package org.javafunk.referee.tree;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.Action;
import org.javafunk.funk.functors.Mapper;

import static org.javafunk.funk.Eagerly.each;
import static org.javafunk.funk.Eagerly.map;
import static org.javafunk.funk.Literals.iterableBuilderWith;
import static org.javafunk.funk.Literals.iterableWith;

@Value
@AllArgsConstructor
public class Node<T> {
    String label;
    T value;
    Iterable<Node<T>> children;

    public Node(String label, T value) {
        this(label, value, Literals.<Node<T>>iterable());
    }

    public <S extends Visitor<T>> S visitDepthFirst(final S visitor) {
        visitor.visit(this);

        each(children, new Action<Node<T>>() {
            @Override public void on(Node<T> node) {
                node.visitDepthFirst(visitor);
            }
        });

        return visitor;
    }

    public <R> Node<R> mapValueDepthFirst(final Mapper<T, R> mapper) {
        return new Node<>(label, mapper.map(value), map(children, new Mapper<Node<T>, Node<R>>() {
            @Override public Node<R> map(Node<T> node) {
                return node.mapValueDepthFirst(mapper);
            }
        }));
    }
}
