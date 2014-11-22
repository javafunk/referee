package org.javafunk.referee.conversion;

public interface CoercionEngine {
    <T> T convertTo(Object instance, Class<T> targetType);
}
