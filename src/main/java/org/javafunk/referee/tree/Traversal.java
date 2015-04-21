package org.javafunk.referee.tree;

public enum Traversal {
    DepthFirst {
        @Override public <T, S extends Visitor<T, S>> S applyTo(Node<T> node, S visitor) {
            return node.visitDepthFirst(visitor);
        }
    },
    BreadthFirst {
        @Override public <T, S extends Visitor<T, S>> S applyTo(Node<T> node, S visitor) {
            return node.visitBreadthFirst(visitor);
        }
    };

    public abstract <T, S extends Visitor<T, S>> S applyTo(Node<T> node, S visitor);
}
