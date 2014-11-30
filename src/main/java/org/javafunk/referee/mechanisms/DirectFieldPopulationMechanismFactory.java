package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.referee.ProblemReport;
import org.javafunk.referee.conversion.CoercionEngine;

import java.util.Map;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectFieldPopulationMechanismFactory implements PopulationMechanismFactory {
    CoercionEngine coercionEngine;

    @Override public <C> ProblemReport validateFor(
            Class<C> targetType,
            Map<String, Object> definition,
            ProblemReport problemReport) {
        return ProblemReport.empty();
    }

    @Override public <C> PopulationMechanism<C> mechanismFor(Class<C> targetType) {
        return new DirectFieldPopulationMechanism<>(targetType, coercionEngine);
    }
}
