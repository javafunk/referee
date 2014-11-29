package org.javafunk.referee;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.mechanisms.PopulationMechanism;
import org.javafunk.referee.mechanisms.PopulationMechanismFactory;

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
}