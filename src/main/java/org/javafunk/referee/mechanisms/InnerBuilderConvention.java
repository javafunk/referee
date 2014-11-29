package org.javafunk.referee.mechanisms;

import lombok.Value;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedMethod;

import static org.javafunk.funk.Eagerly.first;
import static org.javafunk.funk.Literals.iterableFrom;
import static org.javafunk.referee.support.EnrichedClass.toEnrichedClass;

@Value
public class InnerBuilderConvention implements BuilderConvention {
    EnrichedClass<?> builderClass;

    public InnerBuilderConvention(Class<?> targetClass) {
        EnrichedClass<?> enrichedClass = new EnrichedClass<>(targetClass);
        Option<EnrichedClass<?>> possibleBuilderClass = enrichedClass
                .findInnerClassWithName("Builder");

        this.builderClass = possibleBuilderClass.getOrThrow(new RuntimeException());
    }

    @Override public Option<EnrichedMethod> witherFor(String attributeName) {
        return builderClass.findMethodsWithName(witherNameFrom(attributeName)).withOneParameter();
    }

    @Override public Option<EnrichedClass<?>> typeFor(String attributeName) {
        return witherFor(attributeName)
                .map(toParameterType())
                .map(toEnrichedClass());
    }

    @Override public Option<EnrichedMethod> builder() {
        return builderClass
                .findMethodsWithName("build")
                .withNoParameters();
    }

    @Override public Object instance() {
        return builderClass.instantiate();
    }

    private static String witherNameFrom(String attributeName) {
        return "with" +
                attributeName.substring(0, 1).toUpperCase() +
                attributeName.substring(1);
    }

    private static UnaryFunction<EnrichedMethod, Class<?>> toParameterType() {
        return new UnaryFunction<EnrichedMethod, Class<?>>() {
            @Override public Class<?> call(EnrichedMethod method) {
                return first(iterableFrom(method.getParameterTypes())).get();
            }
        };
    }
}
