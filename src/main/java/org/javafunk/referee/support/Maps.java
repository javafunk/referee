package org.javafunk.referee.support;

import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Literals;
import org.javafunk.funk.builders.IterableBuilder;
import org.javafunk.funk.builders.MapBuilder;
import org.javafunk.funk.functors.functions.BinaryFunction;

import java.util.Map;

public class Maps {
    public static <K, V> Map<K, V> select(final Map<K, V> map, Iterable<K> keys) {
        return Eagerly.reduce(keys, Literals.<K, V>mapBuilder(), new BinaryFunction<MapBuilder<K, V>, K, MapBuilder<K, V>>() {
            @Override public MapBuilder<K, V> call(MapBuilder<K, V> mapBuilder, K key) {
                if (map.containsKey(key)) {
                    return mapBuilder.withKeyValuePair(key, map.get(key));
                }
                return mapBuilder;
            }
        }).build();
    }

    public static <K, V> Iterable<V> selectValues(final Map<K, V> map, Iterable<K> keys) {
        return Eagerly.reduce(keys, Literals.<V>iterableBuilder(), new BinaryFunction<IterableBuilder<V>, K, IterableBuilder<V>>() {
            @Override public IterableBuilder<V> call(IterableBuilder<V> iterableBuilder, K key) {
                if (map.containsKey(key)) {
                    return iterableBuilder.with(map.get(key));
                }
                return iterableBuilder;
            }
        }).build();
    }
}
