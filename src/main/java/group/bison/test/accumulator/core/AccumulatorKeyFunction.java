package group.bison.test.accumulator.core;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by diaobisong on 2020/3/29.
 */
@FunctionalInterface
public interface AccumulatorKeyFunction<T, R> extends Function<T, R>, Serializable {
}
