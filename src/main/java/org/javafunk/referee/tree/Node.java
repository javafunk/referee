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
import static org.javafunk.referee.tree.Traversal.DepthFirst;

@Value
@AllArgsConstructor
public class Node<L, T> {
    L label;
    T value;
    Iterable<Node<L, T>> children;

    public static <L, T> Node<L, T> emptyNode(L label) {
        return new Node<>(label, null);
    }

    public static <L, T> Node<L, T> leafNode(L label, T value) {
        return new Node<>(label, value);
    }

    public static <L, T> Node<L, T> branchNode(L label, Iterable<Node<L, T>> children) {
        return new Node<>(label, null, children);
    }

    public static <L, T> Node<L, T> node(L label, T value, Iterable<Node<L, T>> children) {
        return new Node<>(label, value, children);
    }

    public Node(L label, T value) {
        this(label, value, Literals.<Node<L, T>>iterable());
    }

    public <S extends Visitor<L, T, S>> S visit(S visitor) {
        return DepthFirst.applyTo(this, visitor);
    }

    public <S extends Visitor<L, T, S>> S visit(Traversal traversal, S visitor) {
        return traversal.applyTo(this, visitor);
    }

    public <S extends Visitor<L, T, S>> S visitDepthFirst(final S visitor) {
        S updatedVisitor = visitor.visit(this);

        updatedVisitor = Eagerly.reduce(children, updatedVisitor, new BinaryFunction<S, Node<L, T>, S>() {
            @Override public S call(S visitor, Node<L, T> node) {
                return node.visitDepthFirst(visitor);
            }
        });

        return updatedVisitor;
    }

    public <S extends Visitor<L, T, S>> S visitBreadthFirst(S visitor) {
        S updatedVisitor = visitor;
        final Queue<Node<L, T>> nodeQueue = new LinkedList<>(collectionWith(this));

        while (!nodeQueue.isEmpty()) {
            Node<L, T> node = nodeQueue.remove();
            updatedVisitor = updatedVisitor.visit(node);

            each(node.getChildren(), new Action<Node<L, T>>() {
                @Override public void on(Node<L, T> child) {
                    nodeQueue.add(child);
                }
            });
        }

        return updatedVisitor;
    }

    public <R> Node<L, R> mapValue(final Mapper<T, R> mapper) {
        return new Node<>(label, mapper.map(value), map(children, new Mapper<Node<L, T>, Node<L, R>>() {
            @Override public Node<L, R> map(Node<L, T> node) {
                return node.mapValue(mapper);
            }
        }));
    }
}
