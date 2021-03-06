package org.javafunk.referee.mechanisms;

import org.javafunk.referee.ProblemReport;

import java.util.Map;

public interface PopulationMechanismFactory {
    <C> ProblemReport validateFor(Class<C> targetType, Map<Object, Object> definition, ProblemReport problemReport);
    <C> C populateFor(Class<C> targetType, Map<Object, Object> definition);
    <C> PopulationMechanism<C> mechanismFor(Class<C> targetType);
}
