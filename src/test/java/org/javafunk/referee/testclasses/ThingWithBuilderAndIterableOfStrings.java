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
public class ThingWithBuilderAndIterableOfStrings {
    Iterable<String> strings;

    @Wither
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Builder {
        Iterable<String> strings;

        public Builder() { this(iterableOf(String.class)); }

        public ThingWithBuilderAndIterableOfStrings build() {
            return new ThingWithBuilderAndIterableOfStrings(strings);
        }

        @Tolerate
        public Builder withStrings(String first, String... rest) {
            return withStrings(iterableBuilderWith(first).and(rest).build());
        }
    }
}
