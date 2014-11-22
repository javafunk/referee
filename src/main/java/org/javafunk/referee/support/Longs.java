package org.javafunk.referee.support;

import org.javafunk.funk.functors.functions.UnaryFunction;

public class Longs {
    public static UnaryFunction<Integer, Long> integerToLong() {
        return new UnaryFunction<Integer, Long>() {
            @Override public Long call(Integer input) {
                return Long.valueOf(input.toString());
            }
        };
    }
}
