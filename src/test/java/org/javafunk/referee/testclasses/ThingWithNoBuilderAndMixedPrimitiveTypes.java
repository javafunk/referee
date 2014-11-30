package org.javafunk.referee.testclasses;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ThingWithNoBuilderAndMixedPrimitiveTypes {
    String aString;
    Integer anInteger;
    Boolean aBoolean;

    @SuppressWarnings("unused") public ThingWithNoBuilderAndMixedPrimitiveTypes() {
        this(null, null, null);
    }
}
