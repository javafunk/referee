package org.javafunk.referee.testclasses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Value
public class ThingWithBuilderAndStrings {
    String one;
    String two;
    String three;

    @SuppressWarnings("unused") public ThingWithBuilderAndStrings() {
        this(null, null, null);
    }

    public ThingWithBuilderAndStrings(String one, String two, String three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    @Wither
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Builder {
        String one;
        String two;
        String three;

        public Builder() {
            this("First", "Second", "Third");
        }

        public ThingWithBuilderAndStrings build() {
            return new ThingWithBuilderAndStrings(one, two, three);
        }
    }
}
