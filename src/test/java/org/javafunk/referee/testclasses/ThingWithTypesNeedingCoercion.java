package org.javafunk.referee.testclasses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import java.math.BigDecimal;
import java.math.BigInteger;

@Value
@AllArgsConstructor
public class ThingWithTypesNeedingCoercion {
    BigDecimal aBigDecimal;
    BigInteger aBigInteger;
    Long aLong;

    @SuppressWarnings("unused") public ThingWithTypesNeedingCoercion() {
        this(null, null, null);
    }

    @Wither
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Builder {
        BigDecimal aBigDecimal;
        BigInteger aBigInteger;
        Long aLong;

        public Builder() {
            this(new BigDecimal("1000.50"), new BigInteger("1000000000"), 12345L);
        }

        public ThingWithTypesNeedingCoercion build() {
            return new ThingWithTypesNeedingCoercion(aBigDecimal, aBigInteger, aLong);
        }
    }
}
