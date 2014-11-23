package org.javafunk.referee;

import org.javafunk.referee.testclasses.ThingWithMixedPrimitiveTypes;
import org.javafunk.referee.testclasses.ThingWithStrings;
import org.javafunk.referee.testclasses.ThingWithNoBuilder;
import org.javafunk.referee.testclasses.ThingWithTypesNeedingCoercion;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.referee.PopulationEngineBuilder.populationEngine;

public class IntegrationTest {
    @Test
    public void populatesObjectWithOnlyStringFields() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "One: The first string\n" +
                "Two: The second string\n" +
                "Three: The third string");

        // When
        ThingWithStrings result = populationEngine()
                .forType(ThingWithStrings.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithStrings(
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
        ThingWithMixedPrimitiveTypes result = populationEngine()
                .forType(ThingWithMixedPrimitiveTypes.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithMixedPrimitiveTypes(
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
        ThingWithTypesNeedingCoercion result = populationEngine()
                .forType(ThingWithTypesNeedingCoercion.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithTypesNeedingCoercion(
                new BigDecimal("100.56"),
                new BigInteger("1024"),
                12345678L)));
    }

    @Test
    public void usesInnerBuilderDefinedDefaultsWhenNotAllAttributesAreSpecified() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "One: Different first");

        ThingWithStrings builtResult = new ThingWithStrings.Builder().build();

        // When
        ThingWithStrings result = populationEngine()
                .forType(ThingWithStrings.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithStrings(
                "Different first",
                builtResult.getTwo(),
                builtResult.getThree())));
    }

    @Test
    public void usesDirectFieldPopulationIfNoBuilderExists() throws Exception {
        // Given
        Map<String, Object> definition = parse(
                "One: The first string\n" +
                        "Two: The second string\n");

        // When
        ThingWithNoBuilder result = populationEngine()
                .forType(ThingWithNoBuilder.class)
                .process(definition);

        // Then
        assertThat(result, is(new ThingWithNoBuilder(
                "The first string", "The second string")));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parse(String yamlString) {
        return (Map<String, Object>) new Yaml().load(yamlString);
    }
}
