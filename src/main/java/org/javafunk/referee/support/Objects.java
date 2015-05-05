package org.javafunk.referee.support;

import org.javafunk.funk.functors.functions.UnaryFunction;

public class Objects {
    public static <T> UnaryFunction<T, Object> toObject() {
        return new UnaryFunction<T, Object>() {
            @Override public Object call(T thing) {
                return thing;
            }
        };
    }

    public static <T> UnaryFunction<T, Object> toObjectFrom(Class<T> sourceClass) {
        return toObject();
    }
}
