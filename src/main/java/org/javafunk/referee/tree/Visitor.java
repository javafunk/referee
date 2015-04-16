package org.javafunk.referee.tree;


public interface Visitor<T> {
    Visitor<T> visit(Node<T> node);
}
