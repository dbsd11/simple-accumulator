package group.bison.test.data_holdall.drill.service.impl;

import group.bison.test.data_holdall.drill.service.PromethusPush2GatewayService;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by diaobisong on 2020/7/5.
 */
@Service
@Slf4j
public class PromethusPush2GatewayServiceImpl implements PromethusPush2GatewayService, InitializingBean {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${prometheus.pushgateway.intervalInMillis:10000}")
    private Long intervalInMillis;

    @Autowired
    private PushGateway pushGateway;

    @Autowired
    private CollectorRegistry promethusRegistry;

    @Override
    public void pushAll() {
        try {
            pushGateway.push(promethusRegistry, applicationName);
        } catch (Exception e) {
            log.error("pushAll失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                pushAll();
            } catch (Exception e) {
                log.error("scheduled pushAll失败", e);
            }
        }, 5000, intervalInMillis, TimeUnit.MILLISECONDS);
    }
}
