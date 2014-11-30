package org.javafunk.referee.mechanisms;

import org.javafunk.referee.ProblemReport;
import org.javafunk.referee.testclasses.ThingWithBuilderAndStrings;
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
        Class<ThingWithBuilderAndStrings> targetType = ThingWithBuilderAndStrings.class;

        PopulationMechanismFactory first = mock(PopulationMechanismFactory.class);
        PopulationMechanismFactory second = mock(PopulationMechanismFactory.class);
        PopulationMechanismFactory third = mock(PopulationMechanismFactory.class);

        PopulationMechanism<ThingWithBuilderAndStrings> expected =
                (PopulationMechanism<ThingWithBuilderAndStrings>) mock(PopulationMechanism.class);

        given(first.validateFor(targetType, ProblemReport.empty())).willReturn(new ProblemReport(true));
        given(second.validateFor(targetType, ProblemReport.empty())).willReturn(new ProblemReport(false));
        given(third.validateFor(targetType, ProblemReport.empty())).willReturn(new ProblemReport(false));

        given(second.mechanismFor(targetType)).willReturn(expected);

        PopulationMechanismFactory factory =
                new FirstApplicablePopulationMechanismFactory(iterableWith(first, second, third));

        // When
        PopulationMechanism<ThingWithBuilderAndStrings> actual = factory.mechanismFor(ThingWithBuilderAndStrings.class);

        // Then
        assertThat(actual, is(expected));
    }
}