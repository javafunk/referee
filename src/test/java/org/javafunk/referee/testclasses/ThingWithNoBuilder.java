package org.javafunk.referee.testclasses;

import lombok.Value;

@Value
public class ThingWithNoBuilder {
    String one;
    String two;

    @SuppressWarnings("unused") public ThingWithNoBuilder() {
        this(null, null);
    }

    public ThingWithNoBuilder(String one, String two) {
        this.one = one;
        this.two = two;
    }
}
