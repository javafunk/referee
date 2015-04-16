package org.javafunk.referee.mechanisms;

public interface PopulationMechanism<A> {
    PopulationMechanism<A> apply(String attributeName, Object attributeValue);
    PopulationMechanism<A> apply(String attributeName, Object attributeValue, PopulationMechanismFactory factory);
    A getResult();
}
