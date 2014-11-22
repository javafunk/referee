package org.javafunk.referee.support;

import org.javafunk.funk.functors.functions.UnaryFunction;

public class Longs {
    private static final UnaryFunction<Integer, Long> INTEGER_TO_LONG = new UnaryFunction<Integer, Long>() {
        @Override public Long call(Integer input) {
            return Long.valueOf(input.toString());
        }
    };

    private Longs() {}

    public static UnaryFunction<Integer, Long> integerToLong() { return INTEGER_TO_LONG; }
}
