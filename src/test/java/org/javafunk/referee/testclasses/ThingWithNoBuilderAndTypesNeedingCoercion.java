package org.javafunk.referee.testclasses;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.math.BigInteger;

@Value
@AllArgsConstructor
public class ThingWithNoBuilderAndTypesNeedingCoercion {
    BigDecimal aBigDecimal;
    BigInteger aBigInteger;
    Long aLong;

    @SuppressWarnings("unused") public ThingWithNoBuilderAndTypesNeedingCoercion() {
        this(null, null, null);
    }
}
