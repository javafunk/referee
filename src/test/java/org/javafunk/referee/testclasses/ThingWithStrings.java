package org.javafunk.referee.testclasses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Value
public class ThingWithStrings {
    String one;
    String two;
    String three;

    @SuppressWarnings("unused") public ThingWithStrings() {
        this(null, null, null);
    }

    public ThingWithStrings(String one, String two, String three) {
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

        public ThingWithStrings build() {
            return new ThingWithStrings(one, two, three);
        }
    }
}
