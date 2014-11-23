package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.Eagerly;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.functors.predicates.UnaryPredicate;

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

    @Override public <C> boolean canCreateFor(Class<C> targetType) {
        return false;
    }

    @Override public <C> PopulationMechanism<C> forType(Class<C> targetType) {
        return Eagerly.firstMatching(factories, thatCanPopulate(targetType))
                .map(toInstanceFor(targetType))
                .getOrThrow(new RuntimeException());
    }

    private static <C> UnaryFunction<PopulationMechanismFactory, PopulationMechanism<C>> toInstanceFor(
            final Class<C> targetType) {
        return new UnaryFunction<PopulationMechanismFactory, PopulationMechanism<C>>() {
            @Override public PopulationMechanism<C> call(PopulationMechanismFactory factory) {
                return factory.forType(targetType);
            }
        };
    }

    private <C> UnaryPredicate<PopulationMechanismFactory> thatCanPopulate(final Class<C> targetType) {
        return new UnaryPredicate<PopulationMechanismFactory>() {
            @Override public boolean evaluate(PopulationMechanismFactory factory) {
                return factory.canCreateFor(targetType);
            }
        };
    }
}
