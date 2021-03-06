package org.javafunk.referee;

import org.javafunk.referee.testclasses.*;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.referee.Parser.parse;
import static org.javafunk.referee.PopulationEngineBuilder.populationEngine;
import static org.javafunk.referee.Problems.missingInnerBuilderProblem;
import static org.javafunk.referee.Problems.missingWitherProblem;

public class BuilderIntegrationTest {
    @Test
    public void populatesObjectWithOnlyStringFields() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "One: The first string\n" +
                        "Two: The second string\n" +
                        "Three: The third string");

        // When
        PopulationResult<ThingWithBuilderAndStrings> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithBuilderAndStrings.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithBuilderAndStrings(
                "The first string",
                "The second string",
                "The third string")));
    }

    @Test
    public void populatesObjectWithMixedPrimitiveTypes() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "A String: Some sort of string\n" +
                        "An Integer: 100\n" +
                        "A Boolean: true");

        // When
        PopulationResult<ThingWithBuilderAndMixedPrimitiveTypes> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithBuilderAndMixedPrimitiveTypes.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithBuilderAndMixedPrimitiveTypes(
                "Some sort of string",
                100,
                true)));
    }

    @Test
    public void populatesObjectWhereTypesNeedCoercion() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "A BigDecimal: '100.56'\n" +
                        "A BigInteger: 1024\n" +
                        "A Long: 12345678");

        // When
        PopulationResult<ThingWithBuilderAndTypesNeedingCoercion> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithBuilderAndTypesNeedingCoercion.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithBuilderAndTypesNeedingCoercion(
                new BigDecimal("100.56"),
                new BigInteger("1024"),
                12345678L)));
    }

    @Test
    public void populatesObjectWithBuilderAndIterableOfPrimitives() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "Strings:\n" +
                        "  - First\n" +
                        "  - Second");

        // When
        PopulationResult<ThingWithBuilderAndIterableOfStrings> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithBuilderAndIterableOfStrings.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithBuilderAndIterableOfStrings(
                iterableWith("First", "Second"))));
    }

    @Test
    public void populatesObjectWithBuilderAndIterableOfTypeNeedingCoercion() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "Longs:\n" +
                        "  - 1\n" +
                        "  - 2");

        // When
        PopulationResult<ThingWithBuilderAndIterableOfLongs> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithBuilderAndIterableOfLongs.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithBuilderAndIterableOfLongs(
                iterableWith(1L, 2L))));
    }

    @Test
    public void populatesObjectRecursively() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "First Thing: \n" +
                        "  One: Value 1.1\n" +
                        "  Two: Value 1.2\n" +
                        "  Three: Value 1.3\n" +
                        "Second Thing: \n" +
                        "  One: Value 2.1\n" +
                        "  Two: Value 2.2\n" +
                        "  Three: Value 2.3");

        // When
        PopulationResult<ThingWithThingsWithBuilderAndStrings> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithThingsWithBuilderAndStrings.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithThingsWithBuilderAndStrings(
                new ThingWithBuilderAndStrings("Value 1.1", "Value 1.2", "Value 1.3"),
                new ThingWithBuilderAndStrings("Value 2.1", "Value 2.2", "Value 2.3"))));
    }

    @Test
    public void usesInnerBuilderDefinedDefaultsWhenNotAllAttributesAreSpecified() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "One: Different first");

        ThingWithBuilderAndStrings builtResult = new ThingWithBuilderAndStrings.Builder().build();

        // When
        PopulationResult<ThingWithBuilderAndStrings> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithBuilderAndStrings.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithBuilderAndStrings(
                "Different first",
                builtResult.getTwo(),
                builtResult.getThree())));
    }

    @Test
    public void reportsProblemWhenNoInnerBuilderFoundForTargetType() throws Exception {
        Map<Object, Object> definition = parse(
                "One: First\n" +
                        "Two: Second");

        // When
        PopulationResult<ThingWithNoBuilder> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithNoBuilder.class)
                .process(definition);

        // Then
        assertThat(result.getProblemReport(),
                is(ProblemReport.of(
                        missingInnerBuilderProblem("$", ThingWithNoBuilder.class))));
    }

    @Test
    public void reportsProblemWhenNoWitherFoundForAttribute() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "No Wither: Value");

        // When
        PopulationResult<ThingWithBuilderAndMissingWither> result = populationEngine()
                .usingBuilderPopulation()
                .forType(ThingWithBuilderAndMissingWither.class)
                .process(definition);

        // Then
        assertThat(result.getProblemReport(),
                is(ProblemReport.of(
                        missingWitherProblem("$.noWither", ThingWithBuilderAndMissingWither.Builder.class))));
    }
}
