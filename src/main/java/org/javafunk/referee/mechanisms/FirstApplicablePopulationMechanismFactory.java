package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.Eagerly;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.functors.predicates.UnaryPredicate;
import org.javafunk.referee.ProblemReport;
import org.javafunk.referee.Problems;

import java.util.Map;

import static org.javafunk.funk.Literals.iterableFrom;
import static org.javafunk.funk.Literals.mapOf;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FirstApplicablePopulationMechanismFactory implements PopulationMechanismFactory {
    Iterable<PopulationMechanismFactory> factories;

    public FirstApplicablePopulationMechanismFactory(PopulationMechanismFactory... factories) {
        this(iterableFrom(factories));
    }

    @Override public <C> ProblemReport validateFor(
            Class<C> targetType,
            Map<Object, Object> definition,
            ProblemReport problemReport) {
        if (Eagerly.firstMatching(factories, thatCanPopulate(targetType)).hasValue()) {
            return ProblemReport.empty();
        }
        return ProblemReport.of(Problems.noValidMechanism("$", targetType));
    }

    @Override public <C> C populateFor(Class<C> targetType, Map<Object, Object> definition) {
        PopulationMechanism<C> populationMechanism = mechanismFor(targetType);

        for (Map.Entry<Object, Object> attribute : definition.entrySet()) {
            String attributeName = attributeNameFrom(attribute.getKey());
            Object attributeValue = attribute.getValue();

            populationMechanism = populationMechanism.apply(attributeName, attributeValue);
        }
        return populationMechanism.getResult();
    }

    @Override public <C> PopulationMechanism<C> mechanismFor(Class<C> targetType) {
        return Eagerly.firstMatching(factories, thatCanPopulate(targetType))
                .map(toInstanceFor(targetType))
                .getOrThrow(new RuntimeException());
    }

    private String attributeNameFrom(Object attributeNameObject) {
        String attributeNameString = attributeNameObject.toString();
        String joined = attributeNameString.replace(" ", "");
        String firstLetter = attributeNameString.substring(0, 1);
        String attributeName = firstLetter.toLowerCase() + joined.substring(1);

        return attributeName;
    }

    private static <C> UnaryFunction<PopulationMechanismFactory, PopulationMechanism<C>> toInstanceFor(
            final Class<C> targetType) {
        return new UnaryFunction<PopulationMechanismFactory, PopulationMechanism<C>>() {
            @Override public PopulationMechanism<C> call(PopulationMechanismFactory factory) {
                return factory.mechanismFor(targetType);
            }
        };
    }

    private <C> UnaryPredicate<PopulationMechanismFactory> thatCanPopulate(final Class<C> targetType) {
        return new UnaryPredicate<PopulationMechanismFactory>() {
            @Override public boolean evaluate(PopulationMechanismFactory factory) {
                return factory
                        .validateFor(targetType, mapOf(Object.class, Object.class), ProblemReport.empty())
                        .hasNoProblems();
            }
        };
    }
}
