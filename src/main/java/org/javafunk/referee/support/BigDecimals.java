package org.javafunk.referee.support;

import org.javafunk.funk.functors.functions.UnaryFunction;

import java.math.BigDecimal;

public class BigDecimals {
    public static UnaryFunction<String, BigDecimal> stringToBigDecimal() {
        return new UnaryFunction<String, BigDecimal>() {
            @Override public BigDecimal call(String input) {
                return new BigDecimal(input);
            }
        };
    }

    public static UnaryFunction<Double, BigDecimal> doubleToBigDecimal() {
        return new UnaryFunction<Double, BigDecimal>() {
            @Override public BigDecimal call(Double input) {
                return new BigDecimal(input);
            }
        };
    }

    public static UnaryFunction<Integer, BigDecimal> integerToBigDecimal() {
        return new UnaryFunction<Integer, BigDecimal>() {
            @Override public BigDecimal call(Integer input) {
                return new BigDecimal(input);
            }
        };
    }
}
