package org.javafunk.referee.tree.factories.frommap;

import org.javafunk.funk.Literals;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.tree.Node;

public class IndexedElementToNode implements UnaryFunction<Pair<Integer, Object>, Node<Object, Object>> {
    public static IndexedElementToNode fromIndexedElementToNode() {
        return new IndexedElementToNode();
    }

    @Override public Node<Object, Object> call(Pair<Integer, Object> element) {
        return MapEntryToNode.fromMapEntryToNode().call(Literals.<Object, Object>mapEntryFor(
                element.getFirst(),
                element.getSecond()));
    }
}
