package group.bison.test.data_holdall.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * Created by diaobisong on 2020/7/5.
 */
@Component
public class MetricsEntityConventer implements Converter<PGobject, LinkedHashMap>, InitializingBean {

    @Override
    public LinkedHashMap convert(PGobject pGobject) {
        LinkedHashMap<String, String> labelsMap = JSON.parseObject(pGobject.getValue(), new TypeReference<LinkedHashMap<String, String>>() {
        });
        return labelsMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ((DefaultConversionService) DefaultConversionService.getSharedInstance()).addConverter(PGobject.class, LinkedHashMap.class, this);
    }
}
