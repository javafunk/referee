package org.javafunk.referee.tree.traversalhandlers;

import com.google.common.collect.Iterables;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.TraversalHandler;

import static org.javafunk.funk.Literals.iterable;
import static org.javafunk.funk.Literals.iterableBuilderFrom;
import static org.javafunk.funk.Literals.tuple;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TwoZipTraversalHandler<L, T, R>
        extends NoOpTraversalHandler<L, T> {
    Node<L, R> other;
    @NonFinal L label = null;
    @NonFinal Pair<T, R> value = null;
    @NonFinal Iterable<Node<L, Pair<T, R>>> children = iterable();

    public static <L, T, R> TwoZipTraversalHandler<L, T, R> usingZipWith(Node<L, R> other) {
        return new TwoZipTraversalHandler<>(other);
    }

    public TwoZipTraversalHandler(Node<L, R> other) {
        this.other = other;
    }

    @Override public void handleSelf(Node<L, T> self) {
        label = self.getLabel();
        value = tuple(self.getValue(), other.getValue());
    }

    @Override public void handleChild(Integer index, Node<L, T> child) {
        children = iterableBuilderFrom(children)
                .with(child.zip(Iterables.get(other.getChildren(), index)))
                .build();
    }

    @Override public boolean goDeeper() {
        return false;
    }

    public Node<L, Pair<T, R>> getZipped() {
        return Node.node(label, value, children);
    }
}
