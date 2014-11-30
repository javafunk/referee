package org.javafunk.referee.testclasses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Tolerate;
import lombok.experimental.Wither;

import static org.javafunk.funk.Literals.iterableBuilderWith;
import static org.javafunk.funk.Literals.iterableOf;

@Value
public class ThingWithBuilderAndIterableOfLongs {
    Iterable<Long> longs;

    @Wither
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Builder {
        Iterable<Long> longs;

        public Builder() { this(iterableOf(Long.class)); }

        public ThingWithBuilderAndIterableOfLongs build() {
            return new ThingWithBuilderAndIterableOfLongs(longs);
        }

        @Tolerate
        public Builder withLongs(Long first, Long... rest) {
            return withLongs(iterableBuilderWith(first).and(rest).build());
        }
    }
}
