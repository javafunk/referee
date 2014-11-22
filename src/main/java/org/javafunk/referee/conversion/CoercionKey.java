package org.javafunk.referee.conversion;

import lombok.Value;

@Value
public class CoercionKey {
    Class<?> source;
    Class<?> target;

    public static CoercionKey coercionKey(Class<?> source, Class<?> target) {
        return new CoercionKey(source, target);
    }
}
