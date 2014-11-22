package org.javafunk.referee;

public interface PopulationMechanism<A> {
    PopulationMechanism<A> apply(String attributeName, Object attributeValue);
    A getResult();
}
