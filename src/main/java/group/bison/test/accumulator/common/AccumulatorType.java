package group.bison.test.accumulator.common;

import group.bison.test.accumulator.core.Accumulator;
import group.bison.test.accumulator.simple.AvgAccumulator;
import group.bison.test.accumulator.simple.CountAccumulator;
import group.bison.test.accumulator.simple.MaxAccumulator;
import group.bison.test.accumulator.simple.MinAccumulator;
import group.bison.test.accumulator.simple.SumAccumulator;

/**
 * Created by diaobisong on 2020/3/29.
 */
public enum AccumulatorType {

    COUNT(CountAccumulator.class),
    SUM(SumAccumulator.class),
    AVG(AvgAccumulator.class),
    MAX(MaxAccumulator.class),
    MIN(MinAccumulator.class);

    private Class<Accumulator> accumulatorCls;

    AccumulatorType(Class accumulatorCls) {
        this.accumulatorCls = accumulatorCls;
    }

    public Class<Accumulator> getAccumulatorCls() {
        return accumulatorCls;
    }
}
