package org.javafunk.referee.testclasses;

import lombok.Value;

@Value
public class ThingWithNoBuilderAndIterableOfLongs {
    Iterable<Long> longs;

    public ThingWithNoBuilderAndIterableOfLongs() { this(null); }

    public ThingWithNoBuilderAndIterableOfLongs(Iterable<Long> longs) {
        this.longs = longs;
    }
}
