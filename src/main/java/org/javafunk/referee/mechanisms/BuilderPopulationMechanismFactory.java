package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.ProblemReport;
import org.javafunk.referee.attributename.AttributeNameResolver;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.support.EnrichedClass;

import java.util.Map;

import static java.lang.String.format;
import static org.javafunk.referee.Problems.missingInnerBuilderProblem;
import static org.javafunk.referee.Problems.missingWitherProblem;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuilderPopulationMechanismFactory implements PopulationMechanismFactory {
    CoercionEngine coercionEngine;
    AttributeNameResolver attributeNameResolver;

    @Override public <C> ProblemReport validateFor(
            Class<C> targetType,
            Map<Object, Object> definition,
            ProblemReport problemReport) {
        ProblemReport updatedProblemReport = problemReport;

        EnrichedClass<C> enrichedClass = new EnrichedClass<>(targetType);
        Option<EnrichedClass<?>> possibleBuilderClass = enrichedClass
                .findInnerClassWithName("Builder");

        if (possibleBuilderClass.hasValue()) {
            BuilderConvention convention = new InnerBuilderConvention(targetType);

            for (Map.Entry<Object, Object> attribute : definition.entrySet()) {
                String attributeName = attributeNameResolver.resolve(attribute.getKey());
                if (convention.witherFor(attributeName).hasNoValue()) {
                    updatedProblemReport = updatedProblemReport
                            .with(missingWitherProblem(
                                    format("$.%s", attributeName),
                                    possibleBuilderClass.get().getUnderlyingClass()));
                }
            }

            return updatedProblemReport;
        } else {
            return updatedProblemReport.with(missingInnerBuilderProblem("$", targetType));
        }
    }

    @Override public <C> C populateFor(Class<C> targetType, Map<Object, Object> definition) {
        PopulationMechanism<C> populationMechanism = mechanismFor(targetType);

        for (Map.Entry<Object, Object> attribute : definition.entrySet()) {
            String attributeName = attributeNameResolver.resolve(attribute.getKey());
            Object attributeValue = attribute.getValue();

            populationMechanism = populationMechanism.apply(attributeName, attributeValue, this);
        }
        return populationMechanism.getResult();
    }

    @Override public <D> PopulationMechanism<D> mechanismFor(Class<D> targetType) {
        return new BuilderPopulationMechanism<>(targetType, coercionEngine);
    }
}
