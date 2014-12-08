package org.javafunk.referee.testclasses;

import lombok.Value;

@Value
public class ThingWithNoBuilderAndIterableOfLongs {
    Iterable<Long> longs;

    @SuppressWarnings("unused")
    public ThingWithNoBuilderAndIterableOfLongs() { this(null); }

    public ThingWithNoBuilderAndIterableOfLongs(Iterable<Long> longs) {
        this.longs = longs;
    }
}
