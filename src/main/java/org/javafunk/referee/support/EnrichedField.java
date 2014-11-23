package org.javafunk.referee.support;

import lombok.Value;

import java.lang.reflect.Field;

@Value
public class EnrichedField {
    Field underlyingField;

    public Class<?> getType() {
        return underlyingField.getType();
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
}
