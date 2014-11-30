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

import static org.javafunk.funk.Literals.iterableFrom;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FirstApplicablePopulationMechanismFactory implements PopulationMechanismFactory {
    Iterable<PopulationMechanismFactory> factories;

    public FirstApplicablePopulationMechanismFactory(PopulationMechanismFactory... factories) {
        this(iterableFrom(factories));
    }

    @Override public <C> ProblemReport validateFor(Class<C> targetType, ProblemReport problemReport) {
        if (Eagerly.firstMatching(factories, thatCanPopulate(targetType)).hasValue()) {
            return ProblemReport.empty();
        }
        return ProblemReport.with(Problems.noValidMechanism("$", targetType));
    }

    @Override public <C> PopulationMechanism<C> mechanismFor(Class<C> targetType) {
        return Eagerly.firstMatching(factories, thatCanPopulate(targetType))
                .map(toInstanceFor(targetType))
                .getOrThrow(new RuntimeException());
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
                return factory.validateFor(targetType, ProblemReport.empty()).hasNoProblems();
            }
        };
    }
}
