package group.bison.test.data_holdall.drill.service;

import group.bison.test.data_holdall.common.domain.model.Metrics;
import group.bison.test.data_holdall.drill.domain.model.DataDrillContext;
import group.bison.test.data_holdall.drill.domain.model.PageResult;
import group.bison.test.data_holdall.storage.domain.model.MetricsCondition;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by diaobisong on 2020/7/5.
 */
public interface DataMergeService {

    public PageResult<Metrics> merge(List<MetricsCondition> metricsConditionList, DataDrillContext dataDrillContext, PageRequest pageRequest);
}
