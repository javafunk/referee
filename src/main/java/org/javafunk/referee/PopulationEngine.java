package org.javafunk.referee;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javafunk.funk.Literals;
import org.javafunk.funk.UnaryFunctions;
import org.javafunk.funk.functors.functions.UnaryFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static java.lang.String.format;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PopulationEngine<T> {
    private static final Map<Class<?>, UnaryFunction<Object, Object>> coercionMap =
            Literals.<Class<?>, UnaryFunction<Object, Object>>mapBuilder()
                    .withKeyValuePair(BigDecimal.class, new UnaryFunction<Object, Object>() {
                        @Override public Object call(Object input) {
                            if (input instanceof String) {
                                return new BigDecimal((String) input);
                            } else if (input instanceof Integer) {
                                return new BigDecimal((Integer) input);
                            } else if (input instanceof Double) {
                                return new BigDecimal((Double) input);
                            }
                            throw new RuntimeException(
                                    format("Cannot convert %s of type %s to BigDecimal.",
                                            input,
                                            input.getClass().getSimpleName()));
                        }
                    })
                    .withKeyValuePair(BigInteger.class, new UnaryFunction<Object, Object>() {
                        @Override public Object call(Object input) {
                            if (input instanceof String) {
                                return new BigInteger((String) input);
                            } else if (input instanceof Integer) {
                                return new BigInteger(input.toString());
                            }
                            throw new RuntimeException(
                                    format("Cannot convert %s of type %s to BigInteger.",
                                            input,
                                            input.getClass().getSimpleName()));
                        }
                    })
                    .withKeyValuePair(Long.class, new UnaryFunction<Object, Object>() {
                        @Override public Object call(Object input) {
                            if (input instanceof Integer) {
                                return Long.valueOf(input.toString());
                            }
                            return null;
                        }
                    })
                    .withKeyValuePair(String.class, UnaryFunctions.identity())
                    .withKeyValuePair(Integer.class, UnaryFunctions.identity())
                    .withKeyValuePair(Boolean.class, UnaryFunctions.identity())
                    .build();

    Class<T> klass;

    @SuppressWarnings("unchecked")
    public T process(Map<String, Object> definition) {
        T instance = instantiate(klass);

        for (Map.Entry<String, Object> attribute : definition.entrySet()) {
            String fieldName = fieldNameFrom(attribute.getKey());
            Field field = fieldWithName(fieldName);
            Object fieldValue = fieldValueFrom(attribute.getValue(), field.getType());

            setFieldValue(instance, field, fieldValue);
        }

        return instance;
    }

    private Object fieldValueFrom(Object value, Class<?> type) {
        return coercionMap.get(type).call(value);
    }

    private void setFieldValue(T instance, Field field, Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(instance, fieldValue);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Field fieldWithName(String fieldName) {
        try {
            return klass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String fieldNameFrom(String identifier) {
        String joined = identifier.replace(" ", "");
        String firstLetter = identifier.substring(0, 1);
        String fieldName = firstLetter.toLowerCase() + joined.substring(1);

        return fieldName;
    }

    private T instantiate(Class<T> klass) {
        Constructor<T> constructor = noArgumentConstructorFor(klass);
        constructor.setAccessible(true);
        try {
            return constructor.newInstance();
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Constructor<T> noArgumentConstructorFor(Class<T> klass) {
        try {
            return klass.getDeclaredConstructor();
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }
}
