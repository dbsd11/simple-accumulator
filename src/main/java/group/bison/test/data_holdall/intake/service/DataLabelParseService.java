package group.bison.test.data_holdall.intake.service;

import group.bison.test.data_holdall.common.domain.model.Metrics;
import group.bison.test.data_holdall.intake.domain.model.DataInTakeContext;

import java.util.LinkedHashMap;

/**
 * Created by diaobisong on 2020/7/2.
 */
public interface DataLabelParseService {

    LinkedHashMap<String, String> parse(DataInTakeContext dataInTakeContext, Metrics metrics);
}
