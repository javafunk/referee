package org.javafunk.referee.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javafunk.funk.functors.predicates.UnaryPredicate;

import java.lang.reflect.Method;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Methods {
    public static UnaryPredicate<Method> havingName(final String methodName) {
        return new UnaryPredicate<Method>() {
            @Override public boolean evaluate(Method method) {
                return method.getName().equals(methodName);
            }
        };
    }
}
