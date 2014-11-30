package org.javafunk.referee.testclasses;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class ThingWithNoBuilderAndIterableOfStrings {
    Iterable<String> strings;

    public ThingWithNoBuilderAndIterableOfStrings() { this(null); }

    public ThingWithNoBuilderAndIterableOfStrings(Iterable<String> strings) {
        this.strings = strings;
    }
}
