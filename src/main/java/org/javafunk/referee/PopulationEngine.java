package org.javafunk.referee;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.javafunk.referee.conversion.CoercionEngine;

import java.lang.reflect.Field;
import java.util.Map;

import static java.lang.String.format;
import static org.javafunk.funk.Literals.iterableFrom;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PopulationEngine<T> {
    Class<T> klass;
    CoercionEngine coercionEngine;
    PopulationMechanismFactory mechanismFactory;

    // Need to coerce from source type to target type when source is a single value
    //   -> Coercions.attemptFor(value, targetType)
    // Need to determine attribute name from extracted key
    //   -> The needs of this may differ depending on the PopulationMechanism
    // Need to know attribute target type, probably a method of the PopulationMechanism
    // Would be nice to have a target definition detailing all target types and maybe strategy required to populate
    // Need to initialise PopulationMechanism for parent target type
    // Need to apply an attribute value using the PopulationMechanism
    // Need to retrieve result from PopulationMechanism
    // Is there a chain of PopulationMechanisms that get tried in order

    // AttributeApplicator

    @SuppressWarnings("unchecked")
    public T process(Map<String, Object> definition) {
        PopulationMechanism<T> populationMechanism = mechanismFactory.forType(klass);

        for (Map.Entry<String, Object> attribute : definition.entrySet()) {
            String attributeName = attributeNameFrom(attribute.getKey());
            Object attributeValue = attribute.getValue();

            populationMechanism = populationMechanism.apply(attributeName, attributeValue);
        }

        return populationMechanism.getResult();
    }

    private String attributeNameFrom(String identifier) {
        String joined = identifier.replace(" ", "");
        String firstLetter = identifier.substring(0, 1);
        String attributeName = firstLetter.toLowerCase() + joined.substring(1);

        return attributeName;
    }

    private void applyAttributeValue(T instance, Field field, Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(instance, fieldValue);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Field fieldWithName(String fieldName) {
        try {
            return klass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }
}
