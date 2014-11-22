package org.javafunk.referee.conversion;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.functions.UnaryFunction;

import java.util.Map;

import static org.javafunk.funk.Literals.mapBuilderFromEntries;
import static org.javafunk.referee.conversion.CoercionKey.coercionKey;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FunctionBasedCoercionEngine implements CoercionEngine {
    Map<CoercionKey, UnaryFunction<? extends Object, ? extends Object>> coercions;

    public FunctionBasedCoercionEngine() {
        this(Literals.<CoercionKey, UnaryFunction<? extends Object, ? extends Object>>map());
    }

    @SuppressWarnings("unchecked")
    @Override public <T> T convertTo(Object instance, Class<T> targetType) {
        UnaryFunction<Object, Object> function =
                (UnaryFunction<Object, Object>) coercions.get(coercionKey(instance.getClass(), targetType));
        return targetType.cast(function.call(instance));
    }

    public FunctionBasedCoercionEngine registerCoersion(
            Class<?> source,
            Class<?> target,
            UnaryFunction<? extends Object, ? extends Object> converter) {
        return new FunctionBasedCoercionEngine(mapBuilderFromEntries(coercions.entrySet())
                .withKeyValuePair(coercionKey(source, target), converter)
                .build());
    }
}
