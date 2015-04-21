package org.javafunk.referee.tree;

public enum Traversal {
    DepthFirst {
        @Override public <L, T, S extends Visitor<L, T, S>> S applyTo(Node<L, T> node, S visitor) {
            return node.visitDepthFirst(visitor);
        }
    },
    BreadthFirst {
        @Override public <L, T, S extends Visitor<L, T, S>> S applyTo(Node<L, T> node, S visitor) {
            return node.visitBreadthFirst(visitor);
        }
    };

    public abstract <L, T, S extends Visitor<L, T, S>> S applyTo(Node<L, T> node, S visitor);
}
