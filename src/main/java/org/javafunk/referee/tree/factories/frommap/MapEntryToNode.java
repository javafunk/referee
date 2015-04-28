package org.javafunk.referee.tree.factories.frommap;

import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.tree.Node;

import java.util.Map;

import static org.javafunk.referee.tree.factories.frommap.IterableToNode.fromIterableToNode;
import static org.javafunk.referee.tree.Node.branchNode;
import static org.javafunk.referee.tree.Node.leafNode;
import static org.javafunk.referee.tree.factories.frommap.MapToNode.fromMapToNode;

public class MapEntryToNode implements UnaryFunction<Map.Entry<Object, Object>, Node<Object, Object>> {
    public static MapEntryToNode fromMapEntryToNode() {
        return new MapEntryToNode();
    }

    @Override public Node<Object, Object> call(Map.Entry<Object, Object> mapEntry) {
        Object label = mapEntry.getKey();
        Object value = mapEntry.getValue();

        if (value instanceof Map) {
            @SuppressWarnings("unchecked") Map<Object, Object> subMap = (Map<Object, Object>) value;
            return branchNode(label, fromMapToNode().call(subMap).getChildren());
        }

        if (value instanceof Iterable) {
            @SuppressWarnings("unchecked") Iterable<Object> subIterable = (Iterable<Object>) value;
            return branchNode(label, fromIterableToNode().call(subIterable).getChildren());
        }

        return leafNode(label, value);
    }
}
