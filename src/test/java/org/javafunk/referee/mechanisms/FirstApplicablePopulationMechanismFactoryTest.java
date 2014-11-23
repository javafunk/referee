package org.javafunk.referee.mechanisms;

import org.javafunk.referee.testclasses.ThingWithStrings;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class FirstApplicablePopulationMechanismFactoryTest {
    @Test
    @SuppressWarnings("unchecked")
    public void returnsInstanceOfFirstPopulationMechanismThatCanPopulateTargetType() throws Exception {
        // Given
        Class<ThingWithStrings> targetType = ThingWithStrings.class;

        PopulationMechanismFactory first = mock(PopulationMechanismFactory.class);
        PopulationMechanismFactory second = mock(PopulationMechanismFactory.class);
        PopulationMechanismFactory third = mock(PopulationMechanismFactory.class);

        PopulationMechanism<ThingWithStrings> expected =
                (PopulationMechanism<ThingWithStrings>) mock(PopulationMechanism.class);

        given(first.canCreateFor(targetType)).willReturn(false);
        given(second.canCreateFor(targetType)).willReturn(true);
        given(third.canCreateFor(targetType)).willReturn(true);

        given(second.forType(targetType)).willReturn(expected);

        PopulationMechanismFactory factory =
                new FirstApplicablePopulationMechanismFactory(iterableWith(first, second, third));

        // When
        PopulationMechanism<ThingWithStrings> actual = factory.forType(ThingWithStrings.class);

        // Then
        assertThat(actual, is(expected));
    }
}