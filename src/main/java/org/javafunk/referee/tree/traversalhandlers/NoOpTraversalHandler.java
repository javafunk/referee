package org.javafunk.referee.tree.traversalhandlers;

import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.TraversalHandler;

public class NoOpTraversalHandler<L, T>
        implements TraversalHandler<L, T> {
    @Override public void handleSelf(Node<L, T> self) {
        // no-op
    }

    @Override public void handleChild(Integer index, Node<L, T> child) {
        // no-op
    }

    @Override public void handleChildren(Iterable<Node<L, T>> children) {
        // no-op
    }

    @Override public boolean goDeeper(Node<L, T> node) {
        return true;
    }
}
