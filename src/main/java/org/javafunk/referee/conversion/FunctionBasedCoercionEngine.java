package org.javafunk.referee.conversion;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.functions.UnaryFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.javafunk.funk.BigDecimals.*;
import static org.javafunk.funk.BigIntegers.fromIntegerToBigInteger;
import static org.javafunk.funk.BigIntegers.fromStringToBigInteger;
import static org.javafunk.funk.Literals.mapBuilderFromEntries;
import static org.javafunk.funk.Longs.fromIntegerToLong;
import static org.javafunk.funk.UnaryFunctions.identity;
import static org.javafunk.referee.conversion.CoercionKey.coercionKey;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FunctionBasedCoercionEngine implements CoercionEngine {
    @Getter Map<CoercionKey, UnaryFunction<? extends Object, ? extends Object>> coercions;

    public static FunctionBasedCoercionEngine withDefaultCoercions() {
        return new FunctionBasedCoercionEngine(defaultCoercions());
    }

    public static FunctionBasedCoercionEngine withNoCoercions() {
        return new FunctionBasedCoercionEngine(noCoercions());
    }

    public static FunctionBasedCoercionEngine withCoercions(
            Map<CoercionKey, UnaryFunction<? extends Object, ? extends Object>> coercions) {
        return new FunctionBasedCoercionEngine(coercions);
    }

    @SuppressWarnings("unchecked")
    @Override public <T> T convertTo(Object instance, Class<T> targetType) {
        UnaryFunction<Object, Object> function =
                (UnaryFunction<Object, Object>) coercions.get(coercionKey(instance.getClass(), targetType));
        return targetType.cast(function.call(instance));
    }

    public FunctionBasedCoercionEngine registerCoercion(
            Class<?> source,
            Class<?> target,
            UnaryFunction<? extends Object, ? extends Object> converter) {
        return new FunctionBasedCoercionEngine(mapBuilderFromEntries(coercions.entrySet())
                .withKeyValuePair(coercionKey(source, target), converter)
                .build());
    }

    public static Map<CoercionKey, UnaryFunction<?, ?>> defaultCoercions() {
        return Literals.<CoercionKey, UnaryFunction<? extends Object, ? extends Object>>mapBuilder()
                .withKeyValuePair(coercionKey(String.class, BigDecimal.class), fromStringToBigDecimal())
                .withKeyValuePair(coercionKey(Double.class, BigDecimal.class), fromDoubleToBigDecimal())
                .withKeyValuePair(coercionKey(Integer.class, BigDecimal.class), fromIntegerToBigDecimal())
                .withKeyValuePair(coercionKey(String.class, BigInteger.class), fromStringToBigInteger())
                .withKeyValuePair(coercionKey(Integer.class, BigInteger.class), fromIntegerToBigInteger())
                .withKeyValuePair(coercionKey(Integer.class, Long.class), fromIntegerToLong())
                .withKeyValuePair(coercionKey(String.class, String.class), identity())
                .withKeyValuePair(coercionKey(Integer.class, Integer.class), identity())
                .withKeyValuePair(coercionKey(Boolean.class, Boolean.class), identity())
                .build();
    }

    public static Map<CoercionKey, UnaryFunction<?, ?>> noCoercions() {
        return Literals.map();
    }
}
