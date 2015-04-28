package org.javafunk.referee.testclasses;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ThingWithStrings {
    String one;
    String two;
    String three;

    @SuppressWarnings("unused") public ThingWithStrings() {
        this(null, null, null);
    }
}
