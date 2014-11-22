package org.javafunk.referee;

import org.javafunk.referee.support.ThingWithStrings;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PopulationStrategyTest {
    @Test
    public void constructsAnEngineUsingTheStrategyForTheSuppliedClass() throws Exception {
        // Given
        Class<ThingWithStrings> target = ThingWithStrings.class;
        PopulationStrategy populationStrategy = new PopulationStrategy();

        // When
        PopulationEngine<ThingWithStrings> engine = populationStrategy.buildEngineFor(target);

        // Then
        assertThat(engine, is(new PopulationEngine<>(
                target,
                populationStrategy.getCoercionEngine(),
                populationStrategy.getPopulationMechanismFactory())));
    }
}