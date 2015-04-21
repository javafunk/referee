package org.javafunk.referee.tree;


public interface Visitor<T, V> {
    V visit(Node<T> node);
}
