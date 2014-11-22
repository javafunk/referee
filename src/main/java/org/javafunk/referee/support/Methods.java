package org.javafunk.referee.support;

import org.javafunk.funk.functors.predicates.UnaryPredicate;

import java.lang.reflect.Method;

public class Methods {
    private Methods() {}

    public static UnaryPredicate<Method> havingName(final String methodName) {
        return new UnaryPredicate<Method>() {
            @Override public boolean evaluate(Method method) {
                return method.getName().equals(methodName);
            }
        };
    }
}
