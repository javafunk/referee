package org.javafunk.referee;

import org.javafunk.referee.testclasses.ThingWithBuilderAndStrings;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PopulationEngineBuilderTest {
    @Test
    public void constructsAnEngineUsingTheStrategyForTheSuppliedClass() throws Exception {
        // Given
        Class<ThingWithBuilderAndStrings> target = ThingWithBuilderAndStrings.class;
        PopulationEngineBuilder populationEngineBuilder = new PopulationEngineBuilder();

        // When
        PopulationEngine<ThingWithBuilderAndStrings> engine = populationEngineBuilder.forType(target);

        // Then
        assertThat(engine, is(new PopulationEngine<>(
                target,
                populationEngineBuilder.getCoercionEngine(),
                populationEngineBuilder.getPopulationMechanismFactory())));
    }
}