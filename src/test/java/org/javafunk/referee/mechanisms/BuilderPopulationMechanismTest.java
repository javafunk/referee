package org.javafunk.referee.mechanisms;

import org.javafunk.funk.Literals;
import org.javafunk.funk.Mappers;
import org.javafunk.funk.UnaryFunctions;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.PopulationMechanism;
import org.javafunk.referee.conversion.CoercionKey;
import org.javafunk.referee.conversion.FunctionBasedCoercionEngine;
import org.javafunk.referee.support.ThingWithStrings;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.javafunk.funk.Literals.mapWith;
import static org.javafunk.funk.UnaryFunctions.identity;
import static org.javafunk.referee.conversion.CoercionKey.coercionKey;
import static org.javafunk.referee.support.BigDecimals.doubleToBigDecimal;
import static org.javafunk.referee.support.BigDecimals.integerToBigDecimal;
import static org.javafunk.referee.support.BigDecimals.stringToBigDecimal;
import static org.javafunk.referee.support.BigIntegers.integerToBigInteger;
import static org.javafunk.referee.support.BigIntegers.stringToBigInteger;
import static org.javafunk.referee.support.Longs.integerToLong;
import static org.testng.Assert.*;

public class BuilderPopulationMechanismTest {
    @Test
    public void appliesAttributeUsingWitherOnInnerBuilderClass() throws Exception {
        // Given
        String attributeName = "one";
        Object attributeValue = "The first value";

        Map<CoercionKey, UnaryFunction<?, ?>> coercionFunctions =
                Literals.<CoercionKey, UnaryFunction<? extends Object, ? extends Object>>mapBuilder()
                        .withKeyValuePair(coercionKey(String.class, String.class), identity())
                        .build();

        PopulationMechanism<ThingWithStrings> mechanism = new BuilderPopulationMechanism<>(
                ThingWithStrings.class,
                FunctionBasedCoercionEngine.withCoercions(coercionFunctions));

        // When
        PopulationMechanism<ThingWithStrings> updatedMechanism = mechanism.apply(attributeName, attributeValue);

        // Then
        assertThat(updatedMechanism.getResult().getOne(), is("The first value"));
    }
}