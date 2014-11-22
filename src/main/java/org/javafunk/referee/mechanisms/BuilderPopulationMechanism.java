package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.PopulationMechanism;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedMethod;
import org.javafunk.referee.support.EnrichedMethods;

import static org.javafunk.funk.Eagerly.first;
import static org.javafunk.funk.Literals.iterableFrom;
import static org.javafunk.referee.support.EnrichedClass.toInstance;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuilderPopulationMechanism<B> implements PopulationMechanism<B> {
    Class<B> targetType;
    EnrichedClass<?> builderClass;
    Object builderInstance;
    CoercionEngine coercionEngine;

    public BuilderPopulationMechanism(Class<B> targetType, CoercionEngine coercionEngine) {
        this.targetType = targetType;

        EnrichedClass<B> enrichedClass = new EnrichedClass<>(targetType);
        Option<EnrichedClass<?>> possibleBuilderClass = enrichedClass
                .findInnerClassWithName("Builder");

        this.builderClass = possibleBuilderClass.getOrThrow(new RuntimeException());
        this.builderInstance = possibleBuilderClass.map(toInstance()).getOrThrow(new RuntimeException());

        this.coercionEngine = coercionEngine;
    }

    @Override public PopulationMechanism<B> apply(String attributeName, Object attributeValue) {
        Option<EnrichedMethod> attributeWither = withersFrom(builderClass, attributeName).withOneParameter();
        Class<?> attributeTargetType = parameterTypeFrom(attributeWither.get());
        Object coercedAttributeValue = attributeValueFrom(attributeValue, attributeTargetType);

        Object updatedBuilderInstance = applyAttributeValue(builderInstance, attributeWither.get(), coercedAttributeValue);

        return new BuilderPopulationMechanism<>(targetType, builderClass, updatedBuilderInstance, coercionEngine);
    }

    @Override public B getResult() {
        Option<EnrichedMethod> possibleBuildMethod = builderClass
                .findMethodsWithName("build")
                .withNoParameters();
        EnrichedMethod buildMethod = possibleBuildMethod
                .getOrThrow(new RuntimeException());
        Object instance = buildMethod.invokeOn(builderInstance);

        return targetType.cast(instance);
    }

    private static EnrichedMethods withersFrom(EnrichedClass<?> klass, String attributeName) {
        return klass.findMethodsWithName(witherNameFrom(attributeName));
    }

    private static String witherNameFrom(String attributeName) {
        return "with" +
                attributeName.substring(0, 1).toUpperCase() +
                attributeName.substring(1);
    }

    private static Object applyAttributeValue(Object builder, EnrichedMethod wither, Object fieldValue) {
        return wither.invokeOn(builder, fieldValue);
    }

    private static Class<?> parameterTypeFrom(EnrichedMethod attributeWither) {
        return first(iterableFrom(attributeWither.getParameterTypes())).get();
    }

    private Object attributeValueFrom(Object value, Class<?> targetClass) {
        return coercionEngine.convertTo(value, targetClass);
    }
}
