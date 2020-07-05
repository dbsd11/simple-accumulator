package group.bison.test.data_holdall.drill.service;

import group.bison.test.data_holdall.drill.domain.model.DataDrillContext;
import group.bison.test.data_holdall.storage.domain.model.MetricsCondition;

import java.util.List;

/**
 * Created by diaobisong on 2020/7/5.
 */
public interface DataFilterService {

    public List<MetricsCondition> filter(DataDrillContext dataDrillContext);
}
