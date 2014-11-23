package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedField;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectFieldPopulationMechanism<A> implements PopulationMechanism<A> {
    EnrichedClass<A> targetClass;
    A targetInstance;
    CoercionEngine coercionEngine;

    public DirectFieldPopulationMechanism(Class<A> targetClass, CoercionEngine coercionEngine) {
        EnrichedClass<A> enrichedTargetClass = new EnrichedClass<>(targetClass);

        this.targetClass = enrichedTargetClass;
        this.targetInstance = enrichedTargetClass.instantiate();
        this.coercionEngine = coercionEngine;
    }

    @Override public PopulationMechanism<A> apply(String attributeName, Object attributeValue) {
        EnrichedField field = targetClass.findFieldWithName(attributeName).getOrThrow(new RuntimeException());
        Class<?> attributeTargetType = field.getType();
        Object coercedAttributeValue = attributeValueFrom(attributeValue, attributeTargetType);
        A updatedInstance = field.setOn(targetInstance, coercedAttributeValue);

        return new DirectFieldPopulationMechanism<A>(targetClass, updatedInstance, coercionEngine);
    }

    @Override public A getResult() {
        return targetInstance;
    }

    private Object attributeValueFrom(Object value, Class<?> targetClass) {
        return coercionEngine.convertTo(value, targetClass);
    }
}
