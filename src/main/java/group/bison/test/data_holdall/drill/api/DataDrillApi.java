package group.bison.test.data_holdall.drill.api;

import group.bison.test.data_holdall.common.domain.model.Metrics;
import group.bison.test.data_holdall.drill.domain.dto.MetricsDrillDto;

import java.util.List;

/**
 * Created by diaobisong on 2020/7/5.
 */
public interface DataDrillApi {

    public List<Metrics> drill(MetricsDrillDto metricsDrillDto);
}
