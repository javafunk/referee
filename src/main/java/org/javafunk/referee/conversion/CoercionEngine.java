package org.javafunk.referee.conversion;

import org.javafunk.referee.support.EnrichedClass;

public interface CoercionEngine {
    <T> T convertTo(Object instance, EnrichedClass<T> targetType);
}
