package org.javafunk.referee.tree.traversalhandlers;

import lombok.experimental.NonFinal;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.tree.Node;

import static org.javafunk.funk.Literals.*;
import static org.javafunk.funk.monads.Option.option;

public class StrictTwoZipTraversalHandler<L, T, R>
        extends NoOpTraversalHandler<L, T> {
    Node<L, R> other;
    @NonFinal L label = null;
    @NonFinal Pair<Option<T>, Option<R>> value = null;
    @NonFinal Iterable<Node<L, Pair<Option<T>, Option<R>>>> children = iterable();

    public static <L, T, R> StrictTwoZipTraversalHandler<L, T, R> usingStrictZipWith(Node<L, R> other) {
        return new StrictTwoZipTraversalHandler<>(other);
    }

    public StrictTwoZipTraversalHandler(Node<L, R> other) {
        this.other = other;
    }

    @Override public void handleSelf(Node<L, T> self) {
        label = self.getLabel();
        value = tuple(option(self.getValue()), option(other.getValue()));
    }

    @Override public void handleChild(Integer index, Node<L, T> child) {
        Option<Node<L, R>> possibleOtherChild = other.findChildBy(child.getLabel());

        if (possibleOtherChild.hasValue()) {
            children = iterableBuilderFrom(children)
                    .with(child.zipStrict(possibleOtherChild.get()))
                    .build();
        }
    }

    @Override public boolean goDeeper() {
        return false;
    }

    public Node<L, Pair<Option<T>, Option<R>>> getZipped() {
        return Node.node(label, value, children);
    }
}
