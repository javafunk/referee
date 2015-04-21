package org.javafunk.referee;

import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.tree.Node;

import static org.javafunk.referee.IndexedElementToNode.fromIndexedElementToNode;
import static org.javafunk.referee.tree.Node.branchNode;

public class IterableToNode implements UnaryFunction<Iterable<Object>, Node<Object, Object>> {
    public static IterableToNode fromIterableToNode() {
        return new IterableToNode();
    }

    @Override public Node<Object, Object> call(Iterable<Object> iterable) {
        return branchNode("$", Eagerly.map(Lazily.enumerate(iterable), fromIndexedElementToNode()));
    }
}
