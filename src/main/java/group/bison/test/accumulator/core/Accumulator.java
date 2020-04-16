package group.bison.test.accumulator.core;

import group.bison.test.accumulator.simple.SimpleAccumulatorFactory;

/**
 * Created by diaobisong on 2020/3/29.
 */
public interface Accumulator<T, V> {

    default public String name() {
        return SimpleAccumulatorFactory.getInstance().getName(this);
    }

    public int offer(T data);

    public V accumulate(AccumulateModel<T> model);
}
