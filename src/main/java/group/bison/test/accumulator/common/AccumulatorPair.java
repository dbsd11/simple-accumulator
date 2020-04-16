package group.bison.test.accumulator.common;

import java.io.Serializable;

/**
 * Created by diaobisong on 2020/3/30.
 */
public class AccumulatorPair<L, R> implements Serializable {
    private static final long serialVersionUID = -1L;

    private L left;

    private R right;

    public AccumulatorPair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L left() {
        return left;
    }

    public R right() {
        return right;
    }

    public static <L, R> AccumulatorPair of(L left, R rigjt) {
        return new AccumulatorPair(left, rigjt);
    }
}
