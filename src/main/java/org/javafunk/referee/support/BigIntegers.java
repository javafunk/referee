package org.javafunk.referee.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.javafunk.funk.functors.functions.UnaryFunction;

import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BigIntegers {
    private static final UnaryFunction<String, BigInteger> STRING_TO_BIG_INTEGER = new UnaryFunction<String, BigInteger>() {
        @Override public BigInteger call(String input) {
            return new BigInteger(input);
        }
    };
    private static final UnaryFunction<Integer, BigInteger> INTEGER_TO_BIG_INTEGER = new UnaryFunction<Integer, BigInteger>() {
        @Override public BigInteger call(Integer input) {
            return new BigInteger(input.toString());
        }
    };

    public static UnaryFunction<String, BigInteger> stringToBigInteger() { return STRING_TO_BIG_INTEGER; }
    public static UnaryFunction<Integer, BigInteger> integerToBigInteger() { return INTEGER_TO_BIG_INTEGER; }
}
