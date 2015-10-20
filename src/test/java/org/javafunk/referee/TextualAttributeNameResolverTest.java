package org.javafunk.referee;

import org.javafunk.referee.attributename.TextualAttributeNameResolver;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TextualAttributeNameResolverTest {
    @Test
    public void convertsObjectToSnakeCaseAttributeName() {
        assertThat(resolve("Hello"), is("hello"));
        assertThat(resolve("This Is An Attribute Name"), is("thisIsAnAttributeName"));
        assertThat(resolve(objectWithToStringOf("I Am The Walrus")), is("iAmTheWalrus"));
    }

    public Object objectWithToStringOf(final String value) {
        return new Object() {
            @Override
            public String toString() {
                return value;
            }
        };
    };

    public String resolve(Object object) {
        return new TextualAttributeNameResolver().resolve(object);
    }
}