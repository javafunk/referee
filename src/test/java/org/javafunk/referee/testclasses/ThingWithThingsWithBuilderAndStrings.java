package org.javafunk.referee.testclasses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Value
public class ThingWithThingsWithBuilderAndStrings {
    ThingWithBuilderAndStrings firstThing;
    ThingWithBuilderAndStrings secondThing;

    @SuppressWarnings("unused") public ThingWithThingsWithBuilderAndStrings() { this(null, null); }

    public ThingWithThingsWithBuilderAndStrings(
            ThingWithBuilderAndStrings firstThing,
            ThingWithBuilderAndStrings secondThing) {
        this.firstThing = firstThing;
        this.secondThing = secondThing;
    }

    @Wither
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Builder {
        ThingWithBuilderAndStrings firstThing;
        ThingWithBuilderAndStrings secondThing;

        public Builder() {
            this(null, null);
        }

        public ThingWithThingsWithBuilderAndStrings build() {
            return new ThingWithThingsWithBuilderAndStrings(firstThing, secondThing);
        }
    }
}
