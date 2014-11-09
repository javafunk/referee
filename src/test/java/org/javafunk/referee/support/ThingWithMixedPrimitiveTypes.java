package org.javafunk.referee.support;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Value
@AllArgsConstructor
public class ThingWithMixedPrimitiveTypes {
    String aString;
    Integer anInteger;
    Boolean aBoolean;

    @SuppressWarnings("unused") public ThingWithMixedPrimitiveTypes() {
        this(null, null, null);
    }

    @Wither
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Builder {
        String aString;
        Integer anInteger;
        Boolean aBoolean;

        public Builder() {
            this("The String", 10000, false);
        }

        public ThingWithMixedPrimitiveTypes build() {
            return new ThingWithMixedPrimitiveTypes(aString, anInteger, aBoolean);
        }
    }
}
