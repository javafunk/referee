package org.javafunk.referee.mechanisms;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Tolerate;
import lombok.experimental.Wither;
import org.javafunk.funk.matchers.OptionMatchers;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.javafunk.funk.Literals.iterableBuilderWith;
import static org.javafunk.funk.matchers.OptionMatchers.hasNoValueOf;
import static org.javafunk.funk.matchers.OptionMatchers.hasValue;

public class InnerBuilderConventionTest {
    @Value
    public static class ThingWithInnerBuilder {
        String string;
        Integer integer;

        @Wither
        @AllArgsConstructor
        @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
        public static class Builder {
            String string;
            Integer integer;
            Iterable<Long> longs;

            public Builder() { this(null, null, null); }

            public Builder withAttributes(String string, Integer integer) {
                return withString(string).withInteger(integer);
            }

            @Tolerate
            public Builder withLongs(Long first, Long... rest) {
                return withLongs(iterableBuilderWith(first).and(rest).build());
            }

            public ThingWithInnerBuilder build() {
                return new ThingWithInnerBuilder(string, integer);
            }
        }
    }

    @Test
    public void returnsWitherMethodFromInnerBuilderForAttributeName() throws Exception {
        // Given
        InnerBuilderConvention convention = new InnerBuilderConvention(ThingWithInnerBuilder.class);

        Method expectedMethod = ThingWithInnerBuilder.Builder.class
                .getDeclaredMethod("withString", String.class);
        EnrichedMethod expectedEnrichedMethod = new EnrichedMethod(expectedMethod);

        // When
        Option<EnrichedMethod> possibleWither = convention.witherFor("string");

        // Then
        assertThat(possibleWither, hasValue(expectedEnrichedMethod));
    }

    @Test
    public void returnsVarArgsWitherMethodFromInnerBuilderWhenAttributeIsEnumerable() throws Exception {
        // Given
        InnerBuilderConvention convention = new InnerBuilderConvention(ThingWithInnerBuilder.class);

        Method expectedMethod = ThingWithInnerBuilder.Builder.class
                .getDeclaredMethod("withLongs", Long.class, Long[].class);
        EnrichedMethod expectedEnrichedMethod = new EnrichedMethod(expectedMethod);

        // When
        Option<EnrichedMethod> possibleWither = convention.witherFor("longs");

        // Then
        assertThat(possibleWither, hasValue(expectedEnrichedMethod));
    }

    @Test
    public void returnsNoneForWitherWhenBuilderDoesNotHaveAttribute() throws Exception {
        // Given
        InnerBuilderConvention convention = new InnerBuilderConvention(ThingWithInnerBuilder.class);

        // When
        Option<EnrichedMethod> possibleWither = convention.witherFor("randomAttribute");

        // Then
        assertThat(possibleWither, hasNoValueOf(EnrichedMethod.class));
    }

    @Test
    public void returnsParameterTypeWhenWitherHasOnlyOneParameter() throws Exception {
        // Given
        InnerBuilderConvention convention = new InnerBuilderConvention(ThingWithInnerBuilder.class);
        EnrichedClass<?> expectedType = new EnrichedClass<>(Integer.class);

        // When
        Option<EnrichedClass<?>> possibleType = convention.typeOf("integer");

        // Then
        assertThat(possibleType, OptionMatchers.<EnrichedClass<?>>hasValue(expectedType));
    }

    @Test
    public void returnsNoneForTypeWhenBuilderDoesNotHaveAttribute() throws Exception {
        // Given
        InnerBuilderConvention convention = new InnerBuilderConvention(ThingWithInnerBuilder.class);

        // When
        Option<EnrichedClass<?>> possibleType = convention.typeOf("randomAttribute");

        // Then
        assertThat(possibleType, OptionMatchers.<EnrichedClass<?>>hasNoValue());
    }

    @Test
    public void returnsNoneWhenAttributeWitherHasMoreThanOneParameter() throws Exception {
        // Given
        InnerBuilderConvention convention = new InnerBuilderConvention(ThingWithInnerBuilder.class);

        // When
        Option<EnrichedClass<?>> possibleType = convention.typeOf("attributes");

        // Then
        assertThat(possibleType, OptionMatchers.<EnrichedClass<?>>hasNoValue());
    }

    @Test
    public void returnsTrueIfAttributeIsOfAnEnumerableType() throws Exception {
        // Given
        InnerBuilderConvention convention = new InnerBuilderConvention(ThingWithInnerBuilder.class);

        // When
        Boolean isEnumerable = convention.isEnumerable("longs");

        // Then
        assertThat(isEnumerable, is(true));
    }

    @Test
    public void returnsFalseIfAttributeIsNotOfAnEnumerableType() throws Exception {
        // Given
        InnerBuilderConvention convention = new InnerBuilderConvention(ThingWithInnerBuilder.class);

        // When
        Boolean isEnumerable = convention.isEnumerable("string");

        // Then
        assertThat(isEnumerable, is(false));
    }
}