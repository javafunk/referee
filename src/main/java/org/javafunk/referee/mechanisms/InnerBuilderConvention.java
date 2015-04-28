package org.javafunk.referee.mechanisms;

import lombok.Value;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedMethod;
import org.javafunk.referee.support.EnrichedMethods;

import static java.lang.String.format;
import static org.javafunk.funk.Eagerly.first;
import static org.javafunk.funk.Literals.iterableFrom;
import static org.javafunk.referee.support.EnrichedClass.Mappers.toEnrichedClass;

@Value
public class InnerBuilderConvention implements BuilderConvention {
    EnrichedClass<?> builderClass;

    public InnerBuilderConvention(Class<?> targetClass) {
        this(new EnrichedClass<>(targetClass));
    }

    public InnerBuilderConvention(EnrichedClass<?> enrichedClass) {
        Option<EnrichedClass<?>> possibleBuilderClass = enrichedClass
                .findInnerClassWithName("Builder");

        this.builderClass = possibleBuilderClass.getOrThrow(new RuntimeException(format("No Builder on: %s", enrichedClass)));
    }

    @Override public Boolean isEnumerable(String attributeName) {
        return singleParameterWitherFor(attributeName)
                .map(toFirstParameterType())
                .map(toIsEnumerable())
                .getOrElse(false);
    }

    // TODO: Handle the case where there are many variadic methods for the attribute
    @Override public Option<EnrichedMethod> witherFor(String attributeName) {
        if (isEnumerable(attributeName)) {
            return variadicParameterWitherFor(attributeName);
        }
        return singleParameterWitherFor(attributeName);
    }

    @Override public Option<EnrichedClass<?>> typeOf(String attributeName) {
        return witherFor(attributeName)
                .map(toFirstParameterType())
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

    // TODO: Handle the case where there are many one parameter methods for the attribute
    private Option<EnrichedMethod> singleParameterWitherFor(String attributeName) {
        return allWithersFor(attributeName)
                .withOneParameter();
    }

    private Option<EnrichedMethod> variadicParameterWitherFor(String attributeName) {
        return first(allWithersFor(attributeName)
                .withVariadicParameters()
                .all());
    }

    private EnrichedMethods allWithersFor(String attributeName) {
        return builderClass.findMethodsWithName(witherNameFrom(attributeName));
    }

    private static String witherNameFrom(String attributeName) {
        return "with" +
                attributeName.substring(0, 1).toUpperCase() +
                attributeName.substring(1);
    }

    private static UnaryFunction<EnrichedMethod, Class<?>> toFirstParameterType() {
        return new UnaryFunction<EnrichedMethod, Class<?>>() {
            @Override public Class<?> call(EnrichedMethod method) {
                return first(iterableFrom(method.getParameterTypes())).get();
            }
        };
    }

    private static UnaryFunction<Class<?>, Boolean> toIsEnumerable() {
        return new UnaryFunction<Class<?>, Boolean>() {
            @Override public Boolean call(Class<?> klass) {
                return klass.isAssignableFrom(Iterable.class);
            }
        };
    }
}
