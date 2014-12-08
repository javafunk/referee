package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.Problem;
import org.javafunk.referee.ProblemReport;
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

    @Override public <C> ProblemReport validateFor(
            Class<C> targetType,
            Map<String, Object> definition,
            ProblemReport problemReport) {
        ProblemReport updatedProblemReport = problemReport;

        EnrichedClass<C> enrichedClass = new EnrichedClass<>(targetType);
        Option<EnrichedClass<?>> possibleBuilderClass = enrichedClass
                .findInnerClassWithName("Builder");

        if (possibleBuilderClass.hasValue()) {
            BuilderConvention convention = new InnerBuilderConvention(targetType);

            for (Map.Entry<String, Object> attribute : definition.entrySet()) {
                String attributeName = attributeNameFrom(attribute.getKey());
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

    @Override public <D> PopulationMechanism<D> mechanismFor(Class<D> targetType) {
        return new BuilderPopulationMechanism<>(targetType, coercionEngine);
    }

    private String attributeNameFrom(String identifier) {
        String joined = identifier.replace(" ", "");
        String firstLetter = identifier.substring(0, 1);
        String attributeName = firstLetter.toLowerCase() + joined.substring(1);

        return attributeName;
    }
}
