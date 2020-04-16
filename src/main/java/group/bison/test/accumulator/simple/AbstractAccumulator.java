package group.bison.test.accumulator.simple;

import org.apache.commons.collections.CollectionUtils;
import group.bison.test.accumulator.core.Accumulator;
import group.bison.test.accumulator.core.AccumulatorKeyFunction;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by diaobisong on 2020/3/29.
 */
@Slf4j
public abstract class AbstractAccumulator<T, V> implements Accumulator<T, V> {

    protected Object getParameter(T data) {
        List<AccumulatorKeyFunction> accumulatorParaFunctionList = SimpleAccumulatorFactory.getInstance().getAccumulatorParaFunctions(this);
        if (CollectionUtils.isEmpty(accumulatorParaFunctionList)) {
            log.warn("Accumulator:{} 未获取到accumulatorParaFunction 丢弃data", name());
            return null;
        }

        Object parameter = null;
        for (AccumulatorKeyFunction accumulatorParaFunction : accumulatorParaFunctionList) {
            try {
                parameter = accumulatorParaFunction.apply(data);
            } catch (Exception e) {
            }
        }

        if (parameter == null) {
            log.warn("Accumulator:{} 未获取到parameter 丢弃data", name());
        }

        return parameter;
    }
}
