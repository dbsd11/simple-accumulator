package group.bison.test.data_holdall.intake.api;

import group.bison.test.data_holdall.intake.domain.dto.MetricsMessageDto;

/**
 * Created by diaobisong on 2020/7/2.
 */
public interface DataInTakeApi {

    public int intake(MetricsMessageDto metricsMessageDto);
}
