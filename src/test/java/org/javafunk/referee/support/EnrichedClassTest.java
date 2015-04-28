package org.javafunk.referee.support;

import org.javafunk.funk.matchers.OptionMatchers;
import org.javafunk.funk.monads.Option;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.funk.Literals.setWith;
import static org.javafunk.funk.matchers.OptionMatchers.hasValue;
import static org.javafunk.matchbox.Matchers.hasOnlyItemsInAnyOrder;
import static org.javafunk.matchbox.Matchers.hasOnlyItemsInOrder;

public class EnrichedClassTest {
    public static class WithNoParameterConstructor {
        public WithNoParameterConstructor() { }
    }

    public static class WithoutNoParameterConstructor {
        public WithoutNoParameterConstructor(String string) { }
    }

    public static class WithInnerClass {
        public static class Inner {}
    }

    public static class WithoutOtherInnerClasses {
        public static class SomeInner {}
        public static class SomeOtherInner {}
    }

    public static class WithMultipleMethodsWithTheSameName {
        public String compute() { return null; }
        public String compute(String prefix) { return null; }
        public String compute(String prefix, Integer maxLength) { return null; }
        public Integer someOtherMethod() { return null; }
    }

    public static class WithRequiredField {
        private String someField;
        private Integer someOtherField;
    }

    public static class WithSomeFields {
        private String stringField;
        private Object objectField;
        private Integer integerField;
    }

    @Test
    public void findsNoParameterConstructorFromWrappedClassWhenPresent() throws Exception {
        // Given
        Constructor<WithNoParameterConstructor> expected =
                WithNoParameterConstructor.class.getDeclaredConstructor();

        EnrichedClass<WithNoParameterConstructor> enriched =
                new EnrichedClass<>(WithNoParameterConstructor.class);

        // When
        Option<Constructor<WithNoParameterConstructor>> possibleConstructor =
                enriched.findNoParameterConstructor();

        // Then
        assertThat(possibleConstructor, hasValue(expected));
    }

    @Test
    public void returnsNoneWhenWrappedClassHasNoNoParameterConstructor() throws Exception {
        // Given
        EnrichedClass<WithoutNoParameterConstructor> enriched =
                new EnrichedClass<>(WithoutNoParameterConstructor.class);

        // When
        Option<Constructor<WithoutNoParameterConstructor>> possibleConstructor =
                enriched.findNoParameterConstructor();

        // Then
        assertThat(possibleConstructor,
                OptionMatchers.<Constructor<WithoutNoParameterConstructor>>hasNoValue());
    }

    @Test
    public void instantiatesTheUnderlyingClass() throws Exception {
        // Given
        EnrichedClass<WithNoParameterConstructor> enriched =
                new EnrichedClass<>(WithNoParameterConstructor.class);

        // When
        WithNoParameterConstructor instance = enriched.instantiate();

        // Then
        assertThat(instance, instanceOf(WithNoParameterConstructor.class));
    }

    @Test
    public void findsInnerClassWithSuppliedName() throws Exception {
        // Given
        EnrichedClass<WithInnerClass> enriched =
                new EnrichedClass<>(WithInnerClass.class);

        EnrichedClass<?> expectedInnerClass = new EnrichedClass<>(WithInnerClass.Inner.class);

        // When
        Option<EnrichedClass<?>> possibleInnerClass = enriched.findInnerClassWithName("Inner");

        // Then
        assertThat(possibleInnerClass,
                OptionMatchers.<EnrichedClass<?>>hasValue(expectedInnerClass));
    }

    @Test
    public void returnsNoneWhenNoInnerClassHasTheSuppliedName() throws Exception {
        // Given
        EnrichedClass<WithoutOtherInnerClasses> enriched =
                new EnrichedClass<>(WithoutOtherInnerClasses.class);

        // When
        Option<EnrichedClass<?>> possibleInnerClass = enriched.findInnerClassWithName("Inner");

        // Then
        assertThat(possibleInnerClass,
                OptionMatchers.<EnrichedClass<?>>hasNoValue());
    }

    @Test
    public void findsAllMethodsWithTheSuppliedName() throws Exception {
        // Given
        Class<WithMultipleMethodsWithTheSameName> klass = WithMultipleMethodsWithTheSameName.class;

        EnrichedClass<WithMultipleMethodsWithTheSameName> enriched =
                new EnrichedClass<>(klass);

        Set<EnrichedMethod> expected = setWith(
                new EnrichedMethod(klass.getDeclaredMethod("compute")),
                new EnrichedMethod(klass.getDeclaredMethod("compute", String.class)),
                new EnrichedMethod(klass.getDeclaredMethod("compute", String.class, Integer.class)));

        // When
        EnrichedMethods actual = enriched.findMethodsWithName("compute");

        // Then
        assertThat(actual, is(new EnrichedMethods(expected)));
    }

    @Test
    public void findsFieldByName() throws Exception {
        // Given
        Class<WithRequiredField> klass = WithRequiredField.class;

        EnrichedClass<WithRequiredField> enriched = new EnrichedClass<>(klass);

        EnrichedField expected = new EnrichedField(klass.getDeclaredField("someField"));

        // When
        Option<EnrichedField> actual = enriched.findFieldWithName("someField");

        // Then
        assertThat(actual, hasValue(expected));
    }

    @Test
    public void getsAllFields() throws Exception {
        // Given
        Class<WithSomeFields> klass = WithSomeFields.class;

        EnrichedClass<WithSomeFields> enriched = new EnrichedClass<>(klass);

        EnrichedField stringField = new EnrichedField(klass.getDeclaredField("stringField"));
        EnrichedField objectField = new EnrichedField(klass.getDeclaredField("objectField"));
        EnrichedField integerField = new EnrichedField(klass.getDeclaredField("integerField"));

        // When
        Iterable<EnrichedField> fields = enriched.getAllFields();

        // Then
        assertThat(fields, hasOnlyItemsInOrder(stringField, objectField, integerField));
    }
}