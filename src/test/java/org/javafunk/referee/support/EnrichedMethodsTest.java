package org.javafunk.referee.support;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.javafunk.funk.monads.Option;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.setOf;
import static org.javafunk.funk.Literals.setWith;
import static org.javafunk.funk.matchers.OptionMatchers.hasNoValueOf;
import static org.javafunk.funk.matchers.OptionMatchers.hasValue;

public class EnrichedMethodsTest {
    EnrichedMethods enrichedMethods;
    EnrichedMethod enrichedFirst;
    EnrichedMethod enrichedSecond;
    EnrichedMethod enrichedThird;
    EnrichedMethod enrichedFourth;
    EnrichedMethod enrichedFifth;
    EnrichedMethod enrichedSixth;

    public static class ClassWithMethods {
        public void first(String string) {}
        public Integer second(String string, Integer integer) { return 0; }
        public String third(Integer integer) { return ""; }
        public Long fourth() { return 0L; }
        public Integer fifth(Integer first, Integer... rest) { return 0; }
        public String sixth(Integer integer, String... strings) { return ""; }
    }

    @BeforeMethod
    public void setup() {
        Method first = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "first", String.class);
        Method second = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "second", String.class, Integer.class);
        Method third = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "third", Integer.class);
        Method fourth = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "fourth");
        Method fifth = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "fifth", Integer.class, Integer[].class);
        Method sixth = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "sixth", Integer.class, String[].class);

        enrichedFirst = new EnrichedMethod(first);
        enrichedSecond = new EnrichedMethod(second);
        enrichedThird = new EnrichedMethod(third);
        enrichedFourth = new EnrichedMethod(fourth);
        enrichedFifth = new EnrichedMethod(fifth);
        enrichedSixth = new EnrichedMethod(sixth);

        enrichedMethods = new EnrichedMethods(
                setWith(enrichedFirst, enrichedSecond, enrichedThird,
                        enrichedFourth, enrichedFifth, enrichedSixth));
    }

    @Test
    public void findsContainedMethodWithTheSuppliedParameterType() throws Exception {
        // When
        Option<EnrichedMethod> possibleEnrichedMethod = enrichedMethods.withParameterType(Integer.class);

        // Then
        assertThat(possibleEnrichedMethod, hasValue(enrichedThird));
    }

    @Test
    public void returnsNoneWhenNoContainedMethodHasTheSuppliedParameterType() throws Exception {
        // When
        Option<EnrichedMethod> possibleEnrichedMethod = enrichedMethods.withParameterType(Long.class);

        // Then
        assertThat(possibleEnrichedMethod, hasNoValueOf(EnrichedMethod.class));
    }

    @Test
    public void findsContainedMethodWithTheSuppliedParameterTypes() throws Exception {
        // When
        Option<EnrichedMethod> possibleEnrichedMethod = enrichedMethods.withParameterTypes(String.class, Integer.class);

        // Then
        assertThat(possibleEnrichedMethod, hasValue(enrichedSecond));
    }

    @Test
    public void returnsNoneWhenNoContainedMethodHasTheSuppliedParameterTypes() throws Exception {
        // When
        Option<EnrichedMethod> possibleEnrichedMethod = enrichedMethods.withParameterTypes(String.class, Long.class);

        // Then
        assertThat(possibleEnrichedMethod, hasNoValueOf(EnrichedMethod.class));
    }

    @Test
    public void findsContainedMethodWithNoParameters() throws Exception {
        // When
        Option<EnrichedMethod> possibleEnrichedMethod = enrichedMethods.withNoParameters();

        // Then
        assertThat(possibleEnrichedMethod, hasValue(enrichedFourth));
    }

    @Test
    public void returnsNoneWhenNoContainedMethodHasNoParameters() throws Exception {
        // Given
        EnrichedMethods enrichedMethods = new EnrichedMethods(
                setWith(enrichedFirst, enrichedSecond, enrichedThird));

        // When
        Option<EnrichedMethod> possibleEnrichedMethod = enrichedMethods.withNoParameters();

        // Then
        assertThat(possibleEnrichedMethod, hasNoValueOf(EnrichedMethod.class));
    }

    @Test
    public void findsAllContainedMethodsWithVariadicParameters() throws Exception {
        // Given
        EnrichedMethods expected = new EnrichedMethods(setWith(enrichedFifth, enrichedSixth));

        // When
        EnrichedMethods actual = enrichedMethods.withVariadicParameters();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void returnsEmptyEnrichedMethodsWhenNoContainedMethodIsVariadic() throws Exception {
        // Given
        EnrichedMethods enrichedMethods = new EnrichedMethods(
                setWith(enrichedFirst, enrichedSecond, enrichedThird));

        EnrichedMethods expected = new EnrichedMethods(setOf(EnrichedMethod.class));

        // When
        EnrichedMethods actual = enrichedMethods.withVariadicParameters();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void returnsAllContainedMethods() throws Exception {
        // Given
        Set<EnrichedMethod> expected = setWith(enrichedFirst, enrichedSecond, enrichedThird);
        EnrichedMethods enrichedMethods = new EnrichedMethods(expected);

        // When
        Set<EnrichedMethod> actual = enrichedMethods.all();

        // Then
        assertThat(actual, is(expected));
    }
}