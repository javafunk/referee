package org.javafunk.referee.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javafunk.funk.functors.functions.UnaryFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Longs {
    private static final UnaryFunction<Integer, Long> INTEGER_TO_LONG = new UnaryFunction<Integer, Long>() {
        @Override public Long call(Integer input) {
            return Long.valueOf(input.toString());
        }
    };

    public static UnaryFunction<Integer, Long> integerToLong() { return INTEGER_TO_LONG; }
}
