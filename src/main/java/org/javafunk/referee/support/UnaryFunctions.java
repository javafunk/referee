package org.javafunk.referee.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javafunk.funk.functors.functions.UnaryFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnaryFunctions {
    private static final UnaryFunction<? extends Object, ? extends Object> IDENTITY = new UnaryFunction<Object, Object>() {
        @Override public Object call(Object input) {
            return input;
        }
    };

    public static UnaryFunction<? extends Object, ? extends Object> identity() {
        return IDENTITY;
    }
}
