package org.javafunk.referee.support;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.javafunk.funk.monads.Option;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.funk.Literals.setWith;
import static org.javafunk.funk.matchers.OptionMatchers.hasNoValueOf;
import static org.javafunk.funk.matchers.OptionMatchers.hasValue;

public class EnrichedMethodsTest {
    EnrichedMethods enrichedMethods;
    EnrichedMethod enrichedFirst;
    EnrichedMethod enrichedSecond;
    EnrichedMethod enrichedThird;
    EnrichedMethod enrichedFourth;

    public static class ClassWithMethods {
        public void first(String string) {}
        public Integer second(String string, Integer integer) { return 0; }
        public String third(Integer integer) { return ""; }
        public Long fourth() { return 0L; }
    }

    @BeforeMethod
    public void setup() {
        Method first = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "first", String.class);
        Method second = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "second", String.class, Integer.class);
        Method third = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "third", Integer.class);
        Method fourth = MethodUtils.getAccessibleMethod(ClassWithMethods.class, "fourth");

        enrichedFirst = new EnrichedMethod(first);
        enrichedSecond = new EnrichedMethod(second);
        enrichedThird = new EnrichedMethod(third);
        enrichedFourth = new EnrichedMethod(fourth);

        enrichedMethods = new EnrichedMethods(
                setWith(enrichedFirst, enrichedSecond, enrichedThird, enrichedFourth));
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
}