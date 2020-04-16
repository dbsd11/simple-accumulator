package group.bison.test.accumulator.simple;

import group.bison.test.accumulator.common.AccumulatorConstants;
import group.bison.test.accumulator.common.AccumulatorPair;
import group.bison.test.accumulator.common.AccumulatorType;
import group.bison.test.accumulator.core.AccumulateModel;
import group.bison.test.accumulator.core.Accumulator;
import group.bison.test.accumulator.core.AccumulatorFactory;
import group.bison.test.accumulator.core.AccumulatorKeyFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by diaobisong on 2020/3/29.
 */
@Slf4j
public class SimpleAccumulatorFactory implements AccumulatorFactory {

    private Map<String, Accumulator> accumulatorMap = new ConcurrentHashMap<>(64);

    private Map<Integer, String> accumulatorNameMap = new HashMap<>(64);

    private AccumulatorKeyProvider accumulatorKeyProvider = new AccumulatorKeyProvider();

    private static final SimpleAccumulatorFactory INSTANCE = new SimpleAccumulatorFactory();

    private SimpleAccumulatorFactory() {
    }

    @Override
    public <T, R> Accumulator<T, R> create(String name, String type, AccumulateModel<T> accumulateModel) {
        if (accumulatorMap.containsKey(name)) {
            return accumulatorMap.get(name);
        }

        AccumulatorType accumulatorType = null;
        for (AccumulatorType accumulatorTypeValue : AccumulatorType.values()) {
            if (accumulatorTypeValue.name().equalsIgnoreCase(type)) {
                accumulatorType = accumulatorTypeValue;
            }
        }

        if (accumulatorType == null) {
            return null;
        }

        Accumulator accumulator = null;
        try {
            accumulator = accumulatorType.getAccumulatorCls().newInstance();
        } catch (Exception e) {
            log.error("create accumulator with class.newInstance failed:{}", e.getMessage(), e);
        }

        if (accumulator == null) {
            return null;
        }

        Accumulator existAccumulator = accumulatorMap.putIfAbsent(name, accumulator);
        accumulatorNameMap.put(accumulator.hashCode(), name);
        if (existAccumulator != null) {
            accumulator = existAccumulator;
        }

        accumulatorKeyProvider.put(accumulator, accumulateModel);

        return accumulator;
    }

    @Override
    public Accumulator get(String name) {
        return accumulatorMap.get(name);
    }

    public void remove(String name) {
        Accumulator accumulator = accumulatorMap.remove(name);
        if (accumulatorMap != null) {
            accumulatorNameMap.remove(accumulator.hashCode());
        }
        accumulatorKeyProvider.remove(get(name));
    }

    public String getName(Accumulator accumulator) {
        return accumulatorNameMap.get(accumulator.hashCode());
    }

    List<AccumulatorKeyFunction> getAccumulatorParaFunctions(Accumulator accumulator) {
        return accumulatorKeyProvider.getAccumulatorParaFunctions(accumulator);
    }

    List<String> getAccumulatorKeys(Accumulator accumulator, AccumulateModel accumulateModel) {
        return accumulatorKeyProvider.getAccumulatorKeys(accumulator, accumulateModel);
    }

    public static SimpleAccumulatorFactory getInstance() {
        return INSTANCE;
    }

    static class AccumulatorKeyProvider {
        private Map<String, Long> windowSizeMap = new HashMap<>(64);
        private Map<String, List<String>> accumulatorKeyMap = new HashMap<>(64);
        private Map<String, List<AccumulatorKeyFunction>> accumulatorParaMap = new HashMap<>(64);

        AccumulatorKeyProvider() {
        }

        void put(Accumulator accumulator, AccumulateModel accumulateModel) {
            windowSizeMap.put(accumulator.name(), (accumulateModel == null || accumulateModel.getWindowSize() == null) ? AccumulatorConstants.REDIS_KEY_EXPIRE : accumulateModel.getWindowSize());
            accumulatorKeyMap.put(accumulator.name(), (accumulateModel == null || CollectionUtils.isEmpty(accumulateModel.getKeyList())) ? Collections.singletonList("id") : new LinkedList<>(accumulateModel.getKeyList()));
            accumulatorParaMap.put(accumulator.name(), (accumulateModel == null || CollectionUtils.isEmpty(accumulateModel.getParameterFunctionList())) ? Collections.singletonList((AccumulatorKeyFunction<Map, String>) map -> MapUtils.getString(map, "id", null))
                    : new LinkedList<>(accumulateModel.getParameterFunctionList()));
        }

        void remove(Accumulator accumulator) {
            if (accumulator == null) {
                return;
            }

            windowSizeMap.remove(accumulator.name());
            accumulatorKeyMap.remove(accumulator.name());
            accumulatorParaMap.remove(accumulator.name());
        }

        List<AccumulatorKeyFunction> getAccumulatorParaFunctions(Accumulator accumulator) {
            if (accumulator == null || !accumulatorParaMap.containsKey(accumulator.name())) {
                return Collections.emptyList();
            }

            return accumulatorParaMap.get(accumulator.name());
        }

        List<String> getAccumulatorKeys(Accumulator accumulator, AccumulateModel accumulateModel) {
            if (accumulator == null || !accumulatorKeyMap.containsKey(accumulator.name())) {
                return Collections.emptyList();
            }

            List<String> accumulatorKeyList = new LinkedList<>(accumulatorKeyMap.get(accumulator.name()));
            if (accumulateModel != null && CollectionUtils.isNotEmpty(accumulateModel.getKeyFunctionList())) {
                List<AccumulatorPair<AccumulatorKeyFunction, Object>> keyFunctionList = new ArrayList<>(accumulateModel.getKeyFunctionList());
                AtomicInteger i = new AtomicInteger();

                //批量替换key
                accumulatorKeyList = accumulatorKeyList.stream().map(accumulatorKey -> {
                    String newKey = null;
                    Map<String, Object> map = new HashMap<>(16);
                    for (AccumulatorPair<AccumulatorKeyFunction, Object> accumulatorKeyFunctionObjectPair : keyFunctionList) {
                        if (accumulatorKeyFunctionObjectPair.right() == null) {
                            continue;
                        }

                        try {
                            map.put(accumulatorKey, 1);
                            newKey = accumulatorKeyFunctionObjectPair.left().apply(map) != null ? String.valueOf(accumulatorKeyFunctionObjectPair.right()) : null;
                        } catch (Exception e) {
                        } finally {
                            map.remove(accumulatorKey);
                        }
                    }

                    if (newKey == null) {
                        newKey = Optional.ofNullable(keyFunctionList.get(i.getAndIncrement() % keyFunctionList.size()).right()).filter(obj -> obj != null).map(obj -> String.valueOf(obj)).orElse(null);
                    }
                    return StringUtils.defaultString(newKey, accumulatorKey);
                }).collect(Collectors.toList());
            }

            if (windowSizeMap.containsKey(accumulator.name())) {
                accumulatorKeyList.add(String.valueOf(System.currentTimeMillis() / (1000 * windowSizeMap.get(accumulator.name()))));
            }

            accumulatorKeyList.add(0, accumulator.name());

            return accumulatorKeyList;
        }
    }
}
