package org.javafunk.referee.tree;


public interface Visitor<L, T, V> {
    V visit(Node<L, T> node);
}
