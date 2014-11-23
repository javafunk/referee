package org.javafunk.referee.support;

import lombok.Getter;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EnrichedFieldTest {
    public static class ThingWithStringField {
        @Getter String field;
    }

    @Test
    public void returnsTheTypeOfTheField() throws Exception {
        // Given
        Field underlyingField = ThingWithStringField.class.getDeclaredField("field");
        EnrichedField enrichedField = new EnrichedField(underlyingField);

        // When
        Class<?> fieldType = enrichedField.getType();

        // Then
        assertThat(fieldType.equals(String.class), is(true));
    }

    @Test
    public void setsTheProvidedValueOntoTheProvidedInstance() throws Exception {
        // Given
        Field underlyingField = ThingWithStringField.class.getDeclaredField("field");
        EnrichedField enrichedField = new EnrichedField(underlyingField);

        ThingWithStringField thing = new ThingWithStringField();
        String expected = "Some value";

        // When
        ThingWithStringField updatedThing = enrichedField.setOn(thing, expected);

        // Then
        assertThat(updatedThing.getField(), is(expected));
    }
}