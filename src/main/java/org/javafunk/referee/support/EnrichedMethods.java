package org.javafunk.referee.support;

import lombok.Value;
import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.predicates.UnaryPredicate;
import org.javafunk.funk.monads.Option;

import java.util.Set;

import static org.javafunk.referee.support.EnrichedMethod.havingParameterTypes;

@Value
public class EnrichedMethods {
    Set<EnrichedMethod> methods;

    public Option<EnrichedMethod> withParameterType(Class<?> parameterType) {
        return withParameterTypes(Literals.<Class<?>>iterableWith(parameterType));
    }

    public Option<EnrichedMethod> withParameterTypes(Class<?> first, Class<?>... rest) {
        return withParameterTypes(Literals.<Class<?>>iterableBuilderWith(first).and(rest).build());
    }

    public Option<EnrichedMethod> withParameterTypes(Iterable<Class<?>> parameterTypes) {
        return Eagerly.firstMatching(methods, havingParameterTypes(parameterTypes));
    }

    public Option<EnrichedMethod> withNoParameters() {
        return Eagerly.firstMatching(methods, havingParameterTypes(Literals.<Class<?>>iterable()));
    }

    public Option<EnrichedMethod> withOneParameter() {
        return Eagerly.firstMatching(methods, havingASingleParameter());
    }

    private UnaryPredicate<EnrichedMethod> havingASingleParameter() {
        return new UnaryPredicate<EnrichedMethod>() {
            @Override public boolean evaluate(EnrichedMethod method) {
                return method.hasArity(1);
            }
        };
    }
}
