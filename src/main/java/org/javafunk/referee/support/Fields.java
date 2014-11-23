package org.javafunk.referee.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javafunk.funk.functors.predicates.UnaryPredicate;

import java.lang.reflect.Field;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Fields {
    public static UnaryPredicate<Field> havingName(final String fieldName) {
        return new UnaryPredicate<Field>() {
            @Override public boolean evaluate(Field field) {
                return field.getName().equals(fieldName);
            }
        };
    }
}
