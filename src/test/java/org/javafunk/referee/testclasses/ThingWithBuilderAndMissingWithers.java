package org.javafunk.referee.testclasses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Value
public class ThingWithBuilderAndMissingWithers {
    String firstMissingWither;
    String secondMissingWither;
    String presentWither;

    @SuppressWarnings("unused") public ThingWithBuilderAndMissingWithers() {
        this(null, null, null);
    }

    public ThingWithBuilderAndMissingWithers(
            String firstMissingWither,
            String secondMissingWither,
            String presentWither) {
        this.firstMissingWither = firstMissingWither;
        this.secondMissingWither = secondMissingWither;
        this.presentWither = presentWither;
    }

    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Builder {
        String firstMissingWither;
        String secondMissingWither;
        @Wither String presentWither;

        public Builder() {
            this(null, null, null);
        }

        public ThingWithBuilderAndMissingWithers build() {
            return new ThingWithBuilderAndMissingWithers(firstMissingWither, secondMissingWither, presentWither);
        }
    }
}
