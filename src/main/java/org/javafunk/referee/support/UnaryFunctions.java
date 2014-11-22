package org.javafunk.referee.support;

import org.javafunk.funk.functors.functions.UnaryFunction;

public class UnaryFunctions {
    private static final UnaryFunction<? extends Object, ? extends Object> IDENTITY = new UnaryFunction<Object, Object>() {
        @Override public Object call(Object input) {
            return input;
        }
    };

    private UnaryFunctions() {}

    public static UnaryFunction<? extends Object, ? extends Object> identity() {
        return IDENTITY;
    }
}
