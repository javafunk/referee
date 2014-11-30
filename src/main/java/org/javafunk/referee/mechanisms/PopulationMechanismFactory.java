package org.javafunk.referee.mechanisms;

import org.javafunk.referee.ProblemReport;

public interface PopulationMechanismFactory {
    <C> ProblemReport validateFor(Class<C> targetType, ProblemReport problemReport);
    <C> PopulationMechanism<C> mechanismFor(Class<C> targetType);
}
