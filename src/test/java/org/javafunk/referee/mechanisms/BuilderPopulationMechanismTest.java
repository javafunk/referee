package org.javafunk.referee.mechanisms;

import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.conversion.CoercionKey;
import org.javafunk.referee.conversion.FunctionBasedCoercionEngine;
import org.javafunk.referee.testclasses.ThingWithBuilderAndIterableOfStrings;
import org.javafunk.referee.testclasses.ThingWithBuilderAndStrings;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.javafunk.funk.Literals.*;
import static org.javafunk.funk.UnaryFunctions.identity;
import static org.javafunk.referee.conversion.CoercionKey.coercionKey;

public class BuilderPopulationMechanismTest {
    @Test
    public void appliesPrimitiveAttributeUsingWitherOnInnerBuilderClass() throws Exception {
        // Given
        String attributeName = "one";
        Object attributeValue = "The first value";

        Map<CoercionKey, UnaryFunction<?, ?>> coercionFunctions =
                Literals.<CoercionKey, UnaryFunction<? extends Object, ? extends Object>>mapBuilder()
                        .withKeyValuePair(coercionKey(String.class, String.class), identity())
                        .build();

        PopulationMechanism<ThingWithBuilderAndStrings> mechanism = new BuilderPopulationMechanism<>(
                ThingWithBuilderAndStrings.class,
                FunctionBasedCoercionEngine.withCoercions(coercionFunctions));

        // When
        PopulationMechanism<ThingWithBuilderAndStrings> updatedMechanism = mechanism.apply(attributeName, attributeValue);

        // Then
        assertThat(updatedMechanism.getResult().getOne(), is("The first value"));
    }

    @Test
    public void appliesIterableAttributeUsingVarArgsWitherOnInnerBuilderClass() throws Exception {
        // Given
        String attributeName = "strings";
        Iterable<String> attributeValue = listWith("First", "Second");

        Map<CoercionKey, UnaryFunction<?, ?>> coercionFunctions =
                Literals.<CoercionKey, UnaryFunction<? extends Object, ? extends Object>>mapBuilder()
                        .withKeyValuePair(coercionKey(String.class, String.class), identity())
                        .build();

        PopulationMechanism<ThingWithBuilderAndIterableOfStrings> mechanism = new BuilderPopulationMechanism<>(
                ThingWithBuilderAndIterableOfStrings.class,
                FunctionBasedCoercionEngine.withCoercions(coercionFunctions));

        // When
        PopulationMechanism<ThingWithBuilderAndIterableOfStrings> updatedMechanism =
                mechanism.apply(attributeName, attributeValue);

        // Then
        assertThat(updatedMechanism.getResult().getStrings(), is(iterableWith("First", "Second")));
    }
}