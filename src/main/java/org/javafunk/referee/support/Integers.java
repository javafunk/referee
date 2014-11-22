package org.javafunk.referee.support;

import org.javafunk.funk.functors.functions.UnaryFunction;

public class Integers {
    private static final UnaryFunction<Integer, Long> INTEGER_TO_LONG = new UnaryFunction<Integer, Long>() {
        @Override public Long call(Integer integer) {
            return integer.longValue();
        }
    };

    private Integers() {}

    public static UnaryFunction<Integer, Long> integerToLong() {
        return INTEGER_TO_LONG;
    }
}
