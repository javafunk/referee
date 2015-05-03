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

public class DirectFieldIntegrationTest {
    @Test
    public void populatesObjectWithOnlyStringFields() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "One: The first string\n" +
                        "Two: The second string\n" +
                        "Three: The third string");

        // When
        PopulationResult<ThingWithNoBuilderAndStrings> result = populationEngine()
                .usingDirectFieldPopulation()
                .forType(ThingWithNoBuilderAndStrings.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithNoBuilderAndStrings(
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
        PopulationResult<ThingWithNoBuilderAndMixedPrimitiveTypes> result = populationEngine()
                .usingDirectFieldPopulation()
                .forType(ThingWithNoBuilderAndMixedPrimitiveTypes.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithNoBuilderAndMixedPrimitiveTypes(
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
        PopulationResult<ThingWithNoBuilderAndTypesNeedingCoercion> result = populationEngine()
                .usingDirectFieldPopulation()
                .forType(ThingWithNoBuilderAndTypesNeedingCoercion.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithNoBuilderAndTypesNeedingCoercion(
                new BigDecimal("100.56"),
                new BigInteger("1024"),
                12345678L)));
    }

    @Test(enabled = false)
    public void populatesObjectWithBuilderAndIterableOfPrimitives() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "Strings:\n" +
                        "  - First\n" +
                        "  - Second");

        // When
        PopulationResult<ThingWithNoBuilderAndIterableOfStrings> result = populationEngine()
                .usingDirectFieldPopulation()
                .forType(ThingWithNoBuilderAndIterableOfStrings.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithNoBuilderAndIterableOfStrings(
                iterableWith("First", "Second"))));
    }

    @Test(enabled = false)
    public void populatesObjectWithBuilderAndIterableOfTypeNeedingCoercion() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "Longs:\n" +
                        "  - 1\n" +
                        "  - 2");

        // When
        PopulationResult<ThingWithNoBuilderAndIterableOfLongs> result = populationEngine()
                .usingDirectFieldPopulation()
                .forType(ThingWithNoBuilderAndIterableOfLongs.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithNoBuilderAndIterableOfLongs(
                iterableWith(1L, 2L))));
    }

    @Test
    public void usesDirectFieldPopulationIfNoBuilderExists() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "One: The first string\n" +
                        "Two: The second string\n");

        // When
        PopulationResult<ThingWithNoBuilder> result = populationEngine()
                .usingDirectFieldPopulation()
                .forType(ThingWithNoBuilder.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithNoBuilder(
                "The first string", "The second string")));
    }
}
