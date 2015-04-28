package org.javafunk.referee.tree.factories.frommap;

import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.tree.Node;

import java.util.Map;

import static org.javafunk.referee.tree.factories.frommap.MapEntryToNode.fromMapEntryToNode;
import static org.javafunk.referee.tree.Node.branchNode;

public class MapToNode implements UnaryFunction<Map<Object, Object>, Node<Object, Object>> {
    public static MapToNode fromMapToNode() {
        return new MapToNode();
    }

    @Override public Node<Object, Object> call(Map<Object, Object> map) {
        return branchNode("$", Eagerly.map(map.entrySet(), fromMapEntryToNode()));
    }
}
