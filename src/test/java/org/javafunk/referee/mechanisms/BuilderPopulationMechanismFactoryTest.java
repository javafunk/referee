package org.javafunk.referee.mechanisms;

import org.javafunk.referee.conversion.FunctionBasedCoercionEngine;
import org.javafunk.referee.testclasses.ThingWithBuilder;
import org.javafunk.referee.testclasses.ThingWithNoBuilder;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BuilderPopulationMechanismFactoryTest {
    @Test
    public void canCreateIfTargetTypeHasInnerBuilderClass() throws Exception {
        // Given
        Class<ThingWithBuilder> targetType = ThingWithBuilder.class;

        BuilderPopulationMechanismFactory mechanismFactory = new BuilderPopulationMechanismFactory(
                FunctionBasedCoercionEngine.withDefaultCoercions());

        // When
        boolean canCreate = mechanismFactory.canCreateFor(targetType);

        // Then
        assertThat(canCreate, is(true));
    }

    @Test
    public void cannotCreateIfTargetTypeHasNoInnerBuilderClass() throws Exception {
        // Given
        Class<ThingWithNoBuilder> targetType = ThingWithNoBuilder.class;

        BuilderPopulationMechanismFactory mechanismFactory = new BuilderPopulationMechanismFactory(
                FunctionBasedCoercionEngine.withDefaultCoercions());

        // When
        boolean canCreate = mechanismFactory.canCreateFor(targetType);

        // Then
        assertThat(canCreate, is(false));
    }
}