package org.javafunk.referee.conversion;

import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Longs.fromIntegerToLong;
import static org.javafunk.referee.conversion.CoercionKey.coercionKey;

public class FunctionBasedCoercionEngineTest {
    @Test
    public void allowsCoercionsToBeRegisteredIndividually() throws Exception {
        // Given
        Class<?> source = Integer.class;
        Class<?> target = BigInteger.class;
        Coercion coercion = new Coercion() {
            @Override public Object call(Object input) {
                return new BigInteger(input.toString());
            }
        };

        // When
        FunctionBasedCoercionEngine coercionEngine = FunctionBasedCoercionEngine.withNoCoercions()
                .registerCoercion(source, target, coercion);

        // Then
        assertThat(coercionEngine, is(FunctionBasedCoercionEngine.withCoercions(
                Literals.<CoercionKey, UnaryFunction<? extends Object, ? extends Object>>
                        mapWithKeyValuePair(coercionKey(source, target), coercion))));
    }

    @Test
    public void constructsWithDefaultCoercions() throws Exception {
        // Given
        Map<CoercionKey, UnaryFunction<?, ?>> defaultCoercions =
                FunctionBasedCoercionEngine.defaultCoercions();

        FunctionBasedCoercionEngine expected = FunctionBasedCoercionEngine.withCoercions(defaultCoercions);

        // When
        FunctionBasedCoercionEngine actual = FunctionBasedCoercionEngine.withDefaultCoercions();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void constructsWithNoCoercions() throws Exception {
        // Given
        Map<CoercionKey, UnaryFunction<?, ?>> defaultCoercions =
                FunctionBasedCoercionEngine.noCoercions();

        FunctionBasedCoercionEngine expected = FunctionBasedCoercionEngine.withCoercions(defaultCoercions);

        // When
        FunctionBasedCoercionEngine actual = FunctionBasedCoercionEngine.withNoCoercions();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void usesRegisteredCoercionsToCoerceSuppliedObjectToSuppliedType() throws Exception {
        // Given
        Integer input = 126;
        FunctionBasedCoercionEngine coercionEngine = FunctionBasedCoercionEngine
                .withNoCoercions()
                .registerCoercion(Integer.class, Long.class, fromIntegerToLong());

        // When
        Long output = coercionEngine.convertTo(input, Long.class);

        // Then
        assertThat(output, is(126L));
    }
}