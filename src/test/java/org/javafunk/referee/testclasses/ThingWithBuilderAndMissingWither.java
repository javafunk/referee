package org.javafunk.referee.testclasses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Value
public class ThingWithBuilderAndMissingWither {
    String noWither;

    @SuppressWarnings("unused") public ThingWithBuilderAndMissingWither() {
        this(null);
    }

    public ThingWithBuilderAndMissingWither(String noWither) {
        this.noWither = noWither;
    }

    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Builder {
        String noWither;

        public Builder() {
            this(null);
        }

        public ThingWithBuilderAndMissingWither build() {
            return new ThingWithBuilderAndMissingWither(noWither);
        }
    }
}
