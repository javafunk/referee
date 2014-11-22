package org.javafunk.referee.support;

import org.javafunk.funk.functors.predicates.UnaryPredicate;

public class Classes {
    private Classes() {}

    public static UnaryPredicate<Class<?>> havingName(final String className) {
        return new UnaryPredicate<Class<?>>() {
            @Override public boolean evaluate(Class<?> klass) {
                return klass.getSimpleName().equals(className);
            }
        };
    }
}
