package org.javafunk.referee.tree;

import lombok.Value;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.Mapper;

import static org.javafunk.referee.tree.Traversal.DepthFirstPreOrder;

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

    public <R> Tree<L, R> mapValue(Mapper<T, R> mapper) {
        return new Tree<>(rootNode.mapValue(mapper));
    }

    public <R> Tree<L, Pair<T, R>> zip(Tree<L, R> secondTree) {
        return new Tree<>(rootNode.zip(secondTree.getRootNode()));
    }
}
