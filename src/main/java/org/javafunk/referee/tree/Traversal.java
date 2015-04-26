package org.javafunk.referee.tree;

public enum Traversal {
    DepthFirstPreOrder {
        @Override public <L, T, S extends Visitor<L, T, S>> S visit(Node<L, T> node, S visitor) {
            return node.visitDepthFirstPreOrder(visitor);
        }

        @Override public <L, T, H extends TraversalHandler<L, T>> H traverse(Node<L, T> node, H traversalHandler) {
            return node.traverseDepthFirstPreOrder(traversalHandler);
        }
    },
    DepthFirstPostOrder {
        @Override public <L, T, S extends Visitor<L, T, S>> S visit(Node<L, T> node, S visitor) {
            return node.visitDepthFirstPostOrder(visitor);
        }

        @Override public <L, T, H extends TraversalHandler<L, T>> H traverse(Node<L, T> node, H traversalHandler) {
            return node.traverseDepthFirstPostOrder(traversalHandler);
        }
    },
    BreadthFirstLeftToRight {
        @Override public <L, T, S extends Visitor<L, T, S>> S visit(Node<L, T> node, S visitor) {
            return node.visitBreadthFirstLeftToRight(visitor);
        }

        @Override public <L, T, H extends TraversalHandler<L, T>> H traverse(Node<L, T> node, H traversalHandler) {
            return node.traverseBreadthFirstLeftToRight(traversalHandler);
        }
    },
    BreadthFirstRightToLeft {
        @Override public <L, T, S extends Visitor<L, T, S>> S visit(Node<L, T> node, S visitor) {
            return node.visitBreadthFirstRightToLeft(visitor);
        }

        @Override public <L, T, H extends TraversalHandler<L, T>> H traverse(Node<L, T> node, H traversalHandler) {
            return node.traverseBreadthFirstRightToLeft(traversalHandler);
        }
    };

    public abstract <L, T, S extends Visitor<L, T, S>> S visit(Node<L, T> node, S visitor);

    public abstract <L, T, H extends TraversalHandler<L, T>> H traverse(Node<L, T> node, H traversalHandler);
}
