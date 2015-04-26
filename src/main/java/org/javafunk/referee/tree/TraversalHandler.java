package org.javafunk.referee.tree;

public interface TraversalHandler<L, T> {
    void handleSelf(Node<L, T> self);
    void handleChild(Integer index, Node<L, T> child);
    void handleChildren(Iterable<Node<L, T>> children);
    boolean goDeeper();
}
