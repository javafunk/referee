package org.javafunk.referee.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javafunk.funk.functors.predicates.UnaryPredicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Classes {
    public static UnaryPredicate<Class<?>> havingName(final String className) {
        return new UnaryPredicate<Class<?>>() {
            @Override public boolean evaluate(Class<?> klass) {
                return klass.getSimpleName().equals(className);
            }
        };
    }
}
