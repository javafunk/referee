package org.javafunk.referee.mechanisms;

import org.javafunk.referee.ProblemReport;
import org.javafunk.referee.conversion.FunctionBasedCoercionEngine;
import org.javafunk.referee.testclasses.ThingWithBuilder;
import org.javafunk.referee.testclasses.ThingWithNoBuilder;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.mapOf;

public class BuilderPopulationMechanismFactoryTest {
    @Test
    public void canCreateIfTargetTypeHasInnerBuilderClass() throws Exception {
        // Given
        Class<ThingWithBuilder> targetType = ThingWithBuilder.class;
        Map<String, Object> definition = mapOf(String.class, Object.class);

        BuilderPopulationMechanismFactory mechanismFactory = new BuilderPopulationMechanismFactory(
                FunctionBasedCoercionEngine.withDefaultCoercions());

        // When
        ProblemReport report = mechanismFactory.validateFor(targetType, definition, ProblemReport.empty());

        // Then
        assertThat(report.hasProblems(), is(false));
    }

    @Test
    public void cannotCreateIfTargetTypeHasNoInnerBuilderClass() throws Exception {
        // Given
        Class<ThingWithNoBuilder> targetType = ThingWithNoBuilder.class;
        Map<String, Object> definition = mapOf(String.class, Object.class);

        BuilderPopulationMechanismFactory mechanismFactory = new BuilderPopulationMechanismFactory(
                FunctionBasedCoercionEngine.withDefaultCoercions());

        // When
        ProblemReport report = mechanismFactory.validateFor(targetType, definition, ProblemReport.empty());

        // Then
        assertThat(report.hasProblems(), is(true));
    }
}