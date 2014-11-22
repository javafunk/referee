package org.javafunk.referee.support;

import org.javafunk.funk.functors.functions.UnaryFunction;

import java.math.BigInteger;

public class BigIntegers {
    public static UnaryFunction<String, BigInteger> stringToBigInteger() {
        return new UnaryFunction<String, BigInteger>() {
            @Override public BigInteger call(String input) {
                return new BigInteger(input);
            }
        };
    }

    public static UnaryFunction<Integer, BigInteger> integerToBigInteger() {
        return new UnaryFunction<Integer, BigInteger>() {
            @Override public BigInteger call(Integer input) {
                return new BigInteger(input.toString());
            }
        };
    }
}
