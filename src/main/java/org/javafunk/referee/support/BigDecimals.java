package org.javafunk.referee.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javafunk.funk.functors.functions.UnaryFunction;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BigDecimals {
    private static final UnaryFunction<String, BigDecimal> STRING_TO_BIG_DECIMAL = new UnaryFunction<String, BigDecimal>() {
        @Override public BigDecimal call(String input) {
            return new BigDecimal(input);
        }
    };
    private static final UnaryFunction<Double, BigDecimal> DOUBLE_TO_BIG_DECIMAL = new UnaryFunction<Double, BigDecimal>() {
        @Override public BigDecimal call(Double input) {
            return new BigDecimal(input);
        }
    };
    private static final UnaryFunction<Integer, BigDecimal> INTEGER_TO_BIG_DECIMAL = new UnaryFunction<Integer, BigDecimal>() {
        @Override public BigDecimal call(Integer input) {
            return new BigDecimal(input);
        }
    };

    public static UnaryFunction<String, BigDecimal> stringToBigDecimal() { return STRING_TO_BIG_DECIMAL; }
    public static UnaryFunction<Double, BigDecimal> doubleToBigDecimal() { return DOUBLE_TO_BIG_DECIMAL; }
    public static UnaryFunction<Integer, BigDecimal> integerToBigDecimal() { return INTEGER_TO_BIG_DECIMAL; }
}
