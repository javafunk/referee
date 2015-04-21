package org.javafunk.referee.tree;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.Action;
import org.javafunk.funk.functors.Mapper;
import org.javafunk.funk.functors.functions.BinaryFunction;

import java.util.LinkedList;
import java.util.Queue;

import static org.javafunk.funk.Eagerly.each;
import static org.javafunk.funk.Eagerly.map;
import static org.javafunk.funk.Literals.collectionWith;
import static org.javafunk.funk.Literals.iterableBuilderWith;
import static org.javafunk.funk.Literals.iterableWith;

@Value
@AllArgsConstructor
public class Node<T> {
    String label;
    T value;
    Iterable<Node<T>> children;

    public static <T> Node<T> node(String label) {
        return new Node<>(label, null);
    }

    public static <T> Node<T> node(String label, Iterable<Node<T>> children) {
        return new Node<>(label, null, children);
    }

    public static <T> Node<T> node(String label, T value) {
        return new Node<>(label, value);
    }

    public static <T> Node<T> node(String label, T value, Iterable<Node<T>> children) {
        return new Node<>(label, value, children);
    }

    public Node(String label, T value) {
        this(label, value, Literals.<Node<T>>iterable());
    }

    public <S extends Visitor<T, S>> S visit(Traversal traversal, S visitor) {
        return traversal.applyTo(this, visitor);
    }

    public <S extends Visitor<T, S>> S visitDepthFirst(final S visitor) {
        S updatedVisitor = visitor.visit(this);;

        updatedVisitor = Eagerly.reduce(children, updatedVisitor, new BinaryFunction<S, Node<T>, S>() {
            @Override public S call(S visitor, Node<T> node) {
                return node.visitDepthFirst(visitor);
            }
        });

        return updatedVisitor;
    }

    public <S extends Visitor<T, S>> S visitBreadthFirst(S visitor) {
        S updatedVisitor = visitor;
        final Queue<Node<T>> nodeQueue = new LinkedList<>(collectionWith(this));

        while (!nodeQueue.isEmpty()) {
            Node<T> node = nodeQueue.remove();
            updatedVisitor = updatedVisitor.visit(node);

            each(node.getChildren(), new Action<Node<T>>() {
                @Override public void on(Node<T> child) {
                    nodeQueue.add(child);
                }
            });
        }

        return updatedVisitor;
    }

    public <R> Node<R> mapValue(final Mapper<T, R> mapper) {
        return new Node<>(label, mapper.map(value), map(children, new Mapper<Node<T>, Node<R>>() {
            @Override public Node<R> map(Node<T> node) {
                return node.mapValue(mapper);
            }
        }));
    }
}
