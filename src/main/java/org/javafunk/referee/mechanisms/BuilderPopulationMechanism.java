package org.javafunk.referee.mechanisms;

import com.google.common.collect.Iterables;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedMethod;

import java.lang.reflect.Array;
import java.util.Map;

import static org.javafunk.funk.Eagerly.first;
import static org.javafunk.funk.Lazily.rest;
import static org.javafunk.funk.Literals.arrayFrom;
import static org.javafunk.funk.Literals.iterable;
import static org.javafunk.funk.Literals.iterableWith;

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

    // Maybe rather than ask questions of the convention, we get an attribute from the convention and it knows
    // How to handle that attribute

    // Also, if this can get generic enough, we could collapse the mechanism into the engine and just rely on the
    // convention to encapsulate the differences.

    @Override public PopulationMechanism<B> apply(String attributeName, Object attributeValue) {
        return apply(attributeName, attributeValue, null);
    }

    @Override public PopulationMechanism<B> apply(String attributeName, Object attributeValue, PopulationMechanismFactory factory) {
        EnrichedMethod attributeWither = builderConvention.witherFor(attributeName)
                .getOrThrow(new RuntimeException());
        EnrichedClass<?> attributeType = builderConvention.typeOf(attributeName)
                .getOrThrow(new RuntimeException());

        Iterable<Object> arguments;
        if (builderConvention.isEnumerable(attributeName)) {
            Iterable<Object> coercedValues = coerceElementsTo(attributeType, attributeValue);
            arguments = iterableWith(first(coercedValues).get(), toArrayOf(attributeType, rest(coercedValues)));
        } else if (ConventionSelector.appliesTo(attributeType)) {
            @SuppressWarnings("unchecked") Map<Object, Object> attributeValueAsMap = (Map<Object, Object>) attributeValue;
            arguments = iterableWith(factory.populateFor(attributeType.getUnderlyingClass(), attributeValueAsMap));
        } else {
            arguments = iterableWith(coerceTo(attributeValue, attributeType));
        }

        Object updatedBuilderInstance = applyAttributeValue(builderInstance, attributeWither, arguments);

        return new BuilderPopulationMechanism<>(targetType, builderConvention, updatedBuilderInstance, coercionEngine);
    }

    @Override public B getResult() {
        Option<EnrichedMethod> possibleBuildMethod = builderConvention.builder();
        EnrichedMethod buildMethod = possibleBuildMethod
                .getOrThrow(new RuntimeException());
        Object instance = buildMethod.invokeOn(builderInstance);

        return targetType.cast(instance);
    }

    @SuppressWarnings("unchecked")
    private Iterable<Object> coerceElementsTo(EnrichedClass<?> attributeType, Object attributeValue) {
        Iterable<Object> iterableValues = (Iterable<Object>) attributeValue;
        return Lazily.map(iterableValues, coercingTo(attributeType));
    }


    private Object coerceTo(Object attributeValue, EnrichedClass<?> attributeType) {
        return coercionEngine.convertTo(attributeValue, attributeType.getUnderlyingClass());
    }

    private UnaryFunction<Object, Object> coercingTo(final EnrichedClass<?> attributeType) {
        return new UnaryFunction<Object, Object>() {
            @Override public Object call(Object input) {
                return coerceTo(input, attributeType);
            }
        };
    }

    private static Object applyAttributeValue(Object builder, EnrichedMethod wither, Iterable<Object> arguments) {
        return wither.invokeOn(builder, arrayFrom(arguments, Object.class));
    }

    private static Object toArrayOf(EnrichedClass<?> attributeType, Iterable<Object> values) {
        Object array = Array.newInstance(attributeType.getUnderlyingClass(), Iterables.size(values));
        for (Pair<Integer, Object> element : Lazily.enumerate(values)) {
            Array.set(array, element.getFirst(), element.getSecond());
        }
        return array;
    }
}
