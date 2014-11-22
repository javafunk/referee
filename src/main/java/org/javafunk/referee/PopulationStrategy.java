package org.javafunk.referee;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.conversion.CoercionKey;
import org.javafunk.referee.conversion.FunctionBasedCoercionEngine;
import org.javafunk.referee.mechanisms.BuilderPopulationMechanismFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.javafunk.funk.Literals.mapBuilderFromEntries;
import static org.javafunk.funk.UnaryFunctions.identity;
import static org.javafunk.referee.conversion.CoercionKey.coercionKey;
import static org.javafunk.referee.support.BigDecimals.doubleToBigDecimal;
import static org.javafunk.referee.support.BigDecimals.integerToBigDecimal;
import static org.javafunk.referee.support.BigDecimals.stringToBigDecimal;
import static org.javafunk.referee.support.BigIntegers.integerToBigInteger;
import static org.javafunk.referee.support.BigIntegers.stringToBigInteger;
import static org.javafunk.referee.support.Longs.integerToLong;

@Value
@AllArgsConstructor
public class PopulationStrategy {
    FunctionBasedCoercionEngine coercionEngine;
    PopulationMechanismFactory populationMechanismFactory;

    public PopulationStrategy() {
        Map<CoercionKey, UnaryFunction<?, ?>> coercionFunctions =
                Literals.<CoercionKey, UnaryFunction<? extends Object, ? extends Object>>mapBuilder()
                        .withKeyValuePair(coercionKey(String.class, BigDecimal.class), stringToBigDecimal())
                        .withKeyValuePair(coercionKey(Double.class, BigDecimal.class), doubleToBigDecimal())
                        .withKeyValuePair(coercionKey(Integer.class, BigDecimal.class), integerToBigDecimal())
                        .withKeyValuePair(coercionKey(String.class, BigInteger.class), stringToBigInteger())
                        .withKeyValuePair(coercionKey(Integer.class, BigInteger.class), integerToBigInteger())
                        .withKeyValuePair(coercionKey(Integer.class, Long.class), integerToLong())
                        .withKeyValuePair(coercionKey(String.class, String.class), identity())
                        .withKeyValuePair(coercionKey(Integer.class, Integer.class), identity())
                        .withKeyValuePair(coercionKey(Boolean.class, Boolean.class), identity())
                        .build();

        this.coercionEngine = new FunctionBasedCoercionEngine(coercionFunctions);
        this.populationMechanismFactory =
                new BuilderPopulationMechanismFactory(coercionEngine);
    }

    public PopulationStrategy registerCoercion(
            Class<?> source,
            Class<?> target,
            UnaryFunction<? extends Object, ? extends Object> converter) {
        return new PopulationStrategy(
                coercionEngine.registerCoersion(source, target, converter),
                populationMechanismFactory);
    }

    public <T> PopulationEngine<T> buildEngineFor(Class<T> target) {
        return new PopulationEngine<>(target, coercionEngine, populationMechanismFactory);
    }

    public Object convertTo(Object instance, Class<?> targetType) {
        return coercionEngine.convertTo(instance, targetType);
    }

    public <T> PopulationMechanism<T> mechanismFor(Class<T> targetType) {
        return populationMechanismFactory.forType(targetType);
    }
}
