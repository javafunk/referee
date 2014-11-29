package org.javafunk.referee.support;

import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EnrichedMethodTest {
    public static class ClassWithMethods {
        public String variadic(Integer integer, Long... longs) { return ""; }
        public String nonVariadic(Integer integer, Long aLong) { return ""; }
    }

    @Test
    public void returnsTrueForVariadicIfUnderlyingMethodHasAVariadicParameter() throws Exception {
        // Given
        Method variadicMethod = ClassWithMethods.class.getMethod("variadic", Integer.class, Long[].class);
        EnrichedMethod enrichedMethod = new EnrichedMethod(variadicMethod);

        // When
        boolean variadic = enrichedMethod.isVariadic();

        // Then
        assertThat(variadic, is(true));
    }

    @Test
    public void returnsFalseForVariadicIfUnderlyingMethodHasNoVariadicParameters() throws Exception {
        // Given
        Method nonVariadicMethod = ClassWithMethods.class.getMethod("nonVariadic", Integer.class, Long.class);
        EnrichedMethod enrichedMethod = new EnrichedMethod(nonVariadicMethod);

        // When
        boolean variadic = enrichedMethod.isVariadic();

        // Then
        assertThat(variadic, is(false));
    }
}