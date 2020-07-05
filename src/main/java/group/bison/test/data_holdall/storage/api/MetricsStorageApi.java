package group.bison.test.data_holdall.storage.api;

import group.bison.test.data_holdall.drill.domain.model.PageResult;
import group.bison.test.data_holdall.storage.domain.entity.MetricsEntity;
import org.springframework.data.domain.PageRequest;

/**
 * Created by diaobisong on 2020/7/2.
 */
public interface MetricsStorageApi<C> {

    public int write(MetricsEntity metricsEntity);

    public MetricsEntity read(C condition);

    public PageResult<MetricsEntity> readPage(C condition, PageRequest page);
}
