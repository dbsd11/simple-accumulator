package group.bison.test.data_holdall.intake.service;

import group.bison.test.data_holdall.common.domain.model.Metrics;
import group.bison.test.data_holdall.intake.domain.model.DataInTakeContext;

import java.util.List;

/**
 * Created by diaobisong on 2020/7/2.
 */
public interface DataSplitService {

    public List<Metrics> split(DataInTakeContext dataInTakeContext, Metrics metrics);
}
