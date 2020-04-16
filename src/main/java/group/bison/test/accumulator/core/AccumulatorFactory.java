package group.bison.test.accumulator.core;

/**
 * Created by diaobisong on 2020/3/29.
 */
public interface AccumulatorFactory {

    public <T, R> Accumulator<T, R> create(String name, String type, AccumulateModel<T> accumulateModel);

    public Accumulator get(String name);
}
