package org.javafunk.referee.support;

import lombok.Value;
import org.javafunk.funk.functors.functions.UnaryFunction;

import java.lang.reflect.Field;

@Value
public class EnrichedField {
    Field underlyingField;

    public String getName() {
        return underlyingField.getName();
    }

    public EnrichedClass<?> getType() {
        return new EnrichedClass<>(underlyingField.getType());
    }

    public <A> A setOn(A instance, Object value) {
        try {
            underlyingField.setAccessible(true);
            underlyingField.set(instance, value);
            return instance;
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static class Mappers {
        public static UnaryFunction<Field, EnrichedField> toEnrichedField() {
            return new UnaryFunction<Field, EnrichedField>() {
                @Override public EnrichedField call(Field field) {
                    return new EnrichedField(field);
                }
            };
        }
    }
}
