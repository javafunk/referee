package org.javafunk.referee.support;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.monads.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.javafunk.funk.Literals.iterableFrom;
import static org.javafunk.funk.Literals.setFrom;
import static org.javafunk.funk.monads.Option.none;
import static org.javafunk.funk.monads.Option.option;
import static org.javafunk.referee.support.EnrichedField.Mappers.toEnrichedField;
import static org.javafunk.referee.support.EnrichedMethod.toEnrichedMethod;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrichedClass<T> {
    @Getter Class<T> underlyingClass;

    public T instantiate() {
        Constructor<T> constructor = findNoParameterConstructor()
                .getOrThrow(new RuntimeException("No no argument constructor found..."));

        constructor.setAccessible(true);
        try {
            return constructor.newInstance();
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Iterable<EnrichedField> getAllFields() {
        return Lazily.map(iterableFrom(underlyingClass.getDeclaredFields()), toEnrichedField());
    }

    public Option<Constructor<T>> findNoParameterConstructor() {
        try {
            return option(underlyingClass.getDeclaredConstructor());
        } catch (NoSuchMethodException exception) {
            return none();
        }
    }

    public Option<EnrichedClass<?>> findInnerClassWithName(String innerClassName) {
        return Eagerly.firstMatching(iterableFrom(underlyingClass.getClasses()), Classes.havingName(innerClassName))
                .map(Mappers.toEnrichedClass());
    }

    public Option<EnrichedField> findFieldWithName(String fieldName) {
        return Eagerly.firstMatching(iterableFrom(underlyingClass.getDeclaredFields()), Fields.havingName(fieldName))
                .map(toEnrichedField());
    }

    public EnrichedMethods findMethodsWithName(String methodName) {
        return new EnrichedMethods(setFrom(Lazily.map(Lazily.filter(
                iterableFrom(underlyingClass.getDeclaredMethods()),
                Methods.havingName(methodName)), toEnrichedMethod())));
    }

    public static class Mappers {
        public static UnaryFunction<Class<?>, EnrichedClass<?>> toEnrichedClass() {
            return new UnaryFunction<Class<?>, EnrichedClass<?>>() {
                @Override public EnrichedClass<?> call(Class<?> klass) {
                    return new EnrichedClass<>(klass);
                }
            };
        }

        public static UnaryFunction<EnrichedClass<?>, ?> toInstance() {
            return new UnaryFunction<EnrichedClass<?>, Object>() {
                @Override public Object call(EnrichedClass<?> enrichedClass) {
                    return enrichedClass.instantiate();
                }
            };
        }
    }
}
