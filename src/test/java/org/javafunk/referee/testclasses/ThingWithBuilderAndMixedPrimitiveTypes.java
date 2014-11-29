package org.javafunk.referee.testclasses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Value
@AllArgsConstructor
public class ThingWithBuilderAndMixedPrimitiveTypes {
    String aString;
    Integer anInteger;
    Boolean aBoolean;

    @SuppressWarnings("unused") public ThingWithBuilderAndMixedPrimitiveTypes() {
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

        public ThingWithBuilderAndMixedPrimitiveTypes build() {
            return new ThingWithBuilderAndMixedPrimitiveTypes(aString, anInteger, aBoolean);
        }
    }
}
