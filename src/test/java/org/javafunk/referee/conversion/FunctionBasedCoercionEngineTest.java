package org.javafunk.referee.conversion;

import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.referee.conversion.CoercionKey.coercionKey;

public class FunctionBasedCoercionEngineTest {
    @Test
    public void allowsConvertersToBeAddedIndividually() throws Exception {
        // Given
        Class<?> source = Integer.class;
        Class<?> target = BigInteger.class;
        Coercion coercion = new Coercion() {
            @Override public Object call(Object input) {
                return new BigInteger(input.toString());
            }
        };

        // When
        FunctionBasedCoercionEngine coercionEngine = new FunctionBasedCoercionEngine()
                .registerCoersion(source, target, coercion);

        // Then
        assertThat(coercionEngine, is(new FunctionBasedCoercionEngine(
                Literals.<CoercionKey, UnaryFunction<? extends Object, ? extends Object>>
                        mapWithKeyValuePair(coercionKey(source, target), coercion))));
    }
}