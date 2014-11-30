package org.javafunk.referee.testclasses;

import lombok.Value;

@Value
public class ThingWithNoBuilderAndStrings {
    String one;
    String two;
    String three;

    @SuppressWarnings("unused") public ThingWithNoBuilderAndStrings() {
        this(null, null, null);
    }

    public ThingWithNoBuilderAndStrings(String one, String two, String three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }
}
