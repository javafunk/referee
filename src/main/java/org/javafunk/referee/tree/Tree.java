package org.javafunk.referee.tree;

import lombok.Value;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.functions.UnaryFunction;

@Value
public class Tree<L, T> {
    Node<L, T> rootNode;

    public static <L, T> Tree<L, T> tree(Node<L, T> rootNode) {
        return new Tree<>(rootNode);
    }

    public <S extends Visitor<L, T, S>> S visit(S visitor) {
        return rootNode.visit(visitor);
    }

    public <S extends Visitor<L, T, S>> S visit(Traversal traversal, S visitor) {
        return rootNode.visit(traversal, visitor);
    }

    public <H extends TraversalHandler<L, T>> H traverse(H traversalHandler) {
        return rootNode.traverse(traversalHandler);
    }

    public <H extends TraversalHandler<L, T>> H traverse(Traversal traversal, H traversalHandler) {
        return rootNode.traverse(traversal, traversalHandler);
    }

    public <R> Tree<L, R> mapValues(UnaryFunction<T, R> valueMapper) {
        return new Tree<>(rootNode.mapValues(valueMapper));
    }

    public <M> Tree<M, T> mapLabels(UnaryFunction<L, M> labelMapper) {
        return new Tree<>(rootNode.mapLabels(labelMapper));
    }

    public <R> Tree<L, Pair<T, R>> zip(Tree<L, R> secondTree) {
        return new Tree<>(rootNode.zip(secondTree.getRootNode()));
    }
}
