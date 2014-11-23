package org.javafunk.referee.mechanisms;

import org.javafunk.referee.conversion.FunctionBasedCoercionEngine;
import org.javafunk.referee.testclasses.ThingWithNoBuilder;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DirectFieldPopulationMechanismTest {
    @Test
    public void appliesAttributeDirectlyToTheFieldOfAnInstance() throws Exception {
        // Given
        DirectFieldPopulationMechanism<ThingWithNoBuilder> populationMechanism =
                new DirectFieldPopulationMechanism<>(
                        ThingWithNoBuilder.class,
                        FunctionBasedCoercionEngine.withDefaultCoercions());

        String expectedValue = "Some value";

        // When
        PopulationMechanism<ThingWithNoBuilder> updatedMechanism =
                populationMechanism.apply("one", expectedValue);

        // Then
        assertThat(updatedMechanism.getResult().getOne(), is(expectedValue));
    }
}