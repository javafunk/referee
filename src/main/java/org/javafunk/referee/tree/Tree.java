package org.javafunk.referee.tree;

import lombok.Value;
import org.javafunk.funk.functors.Mapper;

import static org.javafunk.referee.tree.Traversal.DepthFirst;

@Value
public class Tree<T> {
    Node<T> rootNode;

    public static <T> Tree<T> tree(Node<T> rootNode) {
        return new Tree<>(rootNode);
    }

    public <S extends Visitor<T, S>> S visit(S visitor) {
        return visit(DepthFirst, visitor);
    }

    public <S extends Visitor<T, S>> S visit(Traversal traversal, S visitor) {
        return rootNode.visit(traversal, visitor);
    }

    public <R> Tree<R> mapValue(Mapper<T, R> mapper) {
        return new Tree<>(rootNode.mapValue(mapper));
    }
}
