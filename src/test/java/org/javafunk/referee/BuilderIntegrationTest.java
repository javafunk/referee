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

public class BuilderIntegrationTest {
    @Test
    public void populatesObjectWithOnlyStringFields() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "One: The first string\n" +
                        "Two: The second string\n" +
                        "Three: The third string");

        // When
        ThingWithBuilderAndStrings result = populationEngine()
                .withBuilderPopulationMechanism()
                .forType(ThingWithBuilderAndStrings.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithBuilderAndStrings(
                "The first string",
                "The second string",
                "The third string")));
    }

    @Test
    public void populatesObjectWithMixedPrimitiveTypes() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "A String: Some sort of string\n" +
                        "An Integer: 100\n" +
                        "A Boolean: true");

        // When
        ThingWithBuilderAndMixedPrimitiveTypes result = populationEngine()
                .withBuilderPopulationMechanism()
                .forType(ThingWithBuilderAndMixedPrimitiveTypes.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithBuilderAndMixedPrimitiveTypes(
                "Some sort of string",
                100,
                true)));
    }

    @Test
    public void populatesObjectWhereTypesNeedCoercion() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "A BigDecimal: '100.56'\n" +
                        "A BigInteger: 1024\n" +
                        "A Long: 12345678");

        // When
        ThingWithBuilderAndTypesNeedingCoercion result = populationEngine()
                .withBuilderPopulationMechanism()
                .forType(ThingWithBuilderAndTypesNeedingCoercion.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithBuilderAndTypesNeedingCoercion(
                new BigDecimal("100.56"),
                new BigInteger("1024"),
                12345678L)));
    }

    @Test
    public void populatesObjectWithBuilderAndIterableOfPrimitives() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "Strings:\n" +
                "  - First\n" +
                "  - Second");

        // When
        ThingWithBuilderAndIterableOfStrings result = populationEngine()
                .withBuilderPopulationMechanism()
                .forType(ThingWithBuilderAndIterableOfStrings.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithBuilderAndIterableOfStrings(
                iterableWith("First", "Second"))));
    }

    @Test
    public void populatesObjectWithBuilderAndIterableOfTypeNeedingCoercion() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "Longs:\n" +
                        "  - 1\n" +
                        "  - 2");

        // When
        ThingWithBuilderAndIterableOfLongs result = populationEngine()
                .withBuilderPopulationMechanism()
                .forType(ThingWithBuilderAndIterableOfLongs.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithBuilderAndIterableOfLongs(
                iterableWith(1L, 2L))));
    }

    @Test
    public void usesInnerBuilderDefinedDefaultsWhenNotAllAttributesAreSpecified() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "One: Different first");

        ThingWithBuilderAndStrings builtResult = new ThingWithBuilderAndStrings.Builder().build();

        // When
        ThingWithBuilderAndStrings result = populationEngine()
                .withBuilderPopulationMechanism()
                .forType(ThingWithBuilderAndStrings.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithBuilderAndStrings(
                "Different first",
                builtResult.getTwo(),
                builtResult.getThree())));
    }
}
