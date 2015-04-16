package org.javafunk.referee.tree;

import lombok.Value;
import org.javafunk.funk.functors.Mapper;

@Value
public class Tree<T> {
    Node<T> rootNode;

    public <S extends Visitor<T>> S visitDepthFirst(S visitor) {
        return rootNode.visitDepthFirst(visitor);
    }

    public <R> Tree<R> mapValueDepthFirst(Mapper<T, R> mapper) {
        return new Tree<>(rootNode.mapValueDepthFirst(mapper));
    }
}
