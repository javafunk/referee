package org.javafunk.referee.mechanisms;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedMethod;

import static org.javafunk.funk.Literals.iterableFrom;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuilderPopulationMechanism<B> implements PopulationMechanism<B> {
    Class<B> targetType;
    BuilderConvention builderConvention;
    Object builderInstance;
    CoercionEngine coercionEngine;

    public BuilderPopulationMechanism(Class<B> targetType, CoercionEngine coercionEngine) {
        this.targetType = targetType;
        this.builderConvention = new InnerBuilderConvention(targetType);
        this.builderInstance = builderConvention.instance();
        this.coercionEngine = coercionEngine;
    }

    // If attribute type is enumerable, need to iterate value and coerce each contained instance
    // Then need to invoke wither passing first and rest

    // If attribute type is not enumerable, need to directly coerce the instance
    // Then need to invoke wither passing just that instance

    @Override public PopulationMechanism<B> apply(String attributeName, Object attributeValue) {
        EnrichedMethod attributeWither = builderConvention.witherFor(attributeName)
                .getOrThrow(new RuntimeException());
        EnrichedClass<?> attributeType = builderConvention.typeFor(attributeName)
                .getOrThrow(new RuntimeException());

        Object coercedAttributeValue = coercionEngine.convertTo(attributeValue, attributeType.getUnderlyingClass());

        Object updatedBuilderInstance = applyAttributeValue(builderInstance, attributeWither, coercedAttributeValue);

        return new BuilderPopulationMechanism<>(targetType, builderConvention, updatedBuilderInstance, coercionEngine);
    }

    @Override public B getResult() {
        Option<EnrichedMethod> possibleBuildMethod = builderConvention.builder();
        EnrichedMethod buildMethod = possibleBuildMethod
                .getOrThrow(new RuntimeException());
        Object instance = buildMethod.invokeOn(builderInstance);

        return targetType.cast(instance);
    }

    private static Object applyAttributeValue(Object builder, EnrichedMethod wither, Object fieldValue) {
        return wither.invokeOn(builder, fieldValue);
    }
}
