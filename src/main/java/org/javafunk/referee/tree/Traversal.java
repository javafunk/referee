package org.javafunk.referee.tree;

public enum Traversal {
    DepthFirstPreOrder {
        @Override public <L, T, S extends Visitor<L, T, S>> S applyTo(Node<L, T> node, S visitor) {
            return node.visitDepthFirstPreOrder(visitor);
        }
    },
    DepthFirstPostOrder {
        @Override public <L, T, S extends Visitor<L, T, S>> S applyTo(Node<L, T> node, S visitor) {
            return node.visitDepthFirstPostOrder(visitor);
        }
    },
    BreadthFirstLeftToRight {
        @Override public <L, T, S extends Visitor<L, T, S>> S applyTo(Node<L, T> node, S visitor) {
            return node.visitBreadthFirstLeftToRight(visitor);
        }
    },
    BreadthFirstRightToLeft {
        @Override public <L, T, S extends Visitor<L, T, S>> S applyTo(Node<L, T> node, S visitor) {
            return node.visitBreadthFirstRightToLeft(visitor);
        }
    };

    public abstract <L, T, S extends Visitor<L, T, S>> S applyTo(Node<L, T> node, S visitor);
}
