package org.javafunk.referee.support;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.functors.predicates.UnaryPredicate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.javafunk.funk.Literals.arrayBuilderOf;
import static org.javafunk.funk.Literals.arrayFrom;
import static org.javafunk.funk.Literals.iterableFrom;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrichedMethod {
    Method underlyingMethod;

    public Object invokeOn(Object instance, Object... arguments) {
        try {
            underlyingMethod.setAccessible(true);
            return underlyingMethod.invoke(instance, arguments);
        } catch (IllegalAccessException|InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean hasParameterTypes(Iterable<Class<?>> parameterTypes) {
        return Arrays.equals(
                underlyingMethod.getParameterTypes(),
                arrayBuilderOf(Class.class).with(parameterTypes).build());
    }

    public boolean hasArity(Integer arity) {
        return underlyingMethod.getParameterTypes().length == arity;
    }

    public Iterable<Class<?>> getParameterTypes() {
        return iterableFrom(underlyingMethod.getParameterTypes());
    }

    public static UnaryFunction<Method, EnrichedMethod> toEnrichedMethod() {
        return new UnaryFunction<Method, EnrichedMethod>() {
            @Override public EnrichedMethod call(Method method) {
                return new EnrichedMethod(method);
            }
        };
    }

    public static UnaryPredicate<EnrichedMethod> havingParameterTypes(final Iterable<Class<?>> parameterTypes) {
        return new UnaryPredicate<EnrichedMethod>() {
            @Override public boolean evaluate(EnrichedMethod method) {
                return method.hasParameterTypes(parameterTypes);
            }
        };
    }
}
