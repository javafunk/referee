package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.ProblemReport;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.support.EnrichedClass;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuilderPopulationMechanismFactory implements PopulationMechanismFactory {
    CoercionEngine coercionEngine;

    @Override public <C> ProblemReport validateFor(Class<C> targetType, ProblemReport problemReport) {
        EnrichedClass<C> enrichedClass = new EnrichedClass<>(targetType);
        Option<EnrichedClass<?>> possibleBuilderClass = enrichedClass
                .findInnerClassWithName("Builder");
        return new ProblemReport(possibleBuilderClass.hasNoValue());
    }

    @Override public <D> PopulationMechanism<D> mechanismFor(Class<D> targetType) {
        return new BuilderPopulationMechanism<>(targetType, coercionEngine);
    }
}
