package group.bison.test.accumulator.core;

import group.bison.test.accumulator.common.AccumulatorPair;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by diaobisong on 2020/3/29.
 */
public class AccumulateModel<T> {

    private Long windowSize;

    private List<String> keyList;

    private List<AccumulatorKeyFunction> parameterFunctionList;

    private List<AccumulatorPair<AccumulatorKeyFunction, Object>> keyFunctionList;

    private AccumulateModel() {
    }

    public Long getWindowSize() {
        return windowSize;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public List<AccumulatorKeyFunction> getParameterFunctionList() {
        return parameterFunctionList;
    }

    public List<AccumulatorPair<AccumulatorKeyFunction, Object>> getKeyFunctionList() {
        return keyFunctionList;
    }

    public static class AccumulateModelBuilder<T> {
        private List<String> keyList;
        private Long windowSize;
        private List<AccumulatorKeyFunction> parameterFunctionList;
        private List<AccumulatorPair<AccumulatorKeyFunction, Object>> keyFunctionList;

        public AccumulateModelBuilder(List<String> keyList) {
            this.keyList = CollectionUtils.isEmpty(keyList) ? new LinkedList<>() : new LinkedList<>(keyList);
            this.keyFunctionList = new LinkedList<>();
            this.parameterFunctionList = new LinkedList<>();
        }

        public AccumulateModelBuilder<T> window(Long windowSize) {
            this.windowSize = windowSize;
            return this;
        }

        public AccumulateModelBuilder<T> parameter(AccumulatorKeyFunction<T, ?> function) {
            parameterFunctionList.addAll(Arrays.asList(function));
            return this;
        }

        public AccumulateModelBuilder<T> eq(AccumulatorKeyFunction<T, ?> function, Object val) {
            keyFunctionList.add(AccumulatorPair.of(function, val));
            return this;
        }

        public AccumulateModel build() {
            AccumulateModel accumulateModel = new AccumulateModel();
            accumulateModel.windowSize = windowSize;
            accumulateModel.keyList = keyList;
            accumulateModel.parameterFunctionList = parameterFunctionList;
            accumulateModel.keyFunctionList = keyFunctionList;
            return accumulateModel;
        }
    }
}
