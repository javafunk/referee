package org.javafunk.referee.tree.traversalhandlers;

import lombok.experimental.NonFinal;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.Literals;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.Maps;
import org.javafunk.referee.tree.Node;

import java.util.Map;
import java.util.Set;

import static org.javafunk.funk.Literals.*;
import static org.javafunk.funk.Literals.mapFromPairs;
import static org.javafunk.funk.Sets.difference;
import static org.javafunk.funk.Sets.intersection;
import static org.javafunk.funk.monads.Option.option;

public class LooseTwoZipTraversalHandler<L, T, R>
        extends NoOpTraversalHandler<L, T> {
    Node<L, R> other;
    @NonFinal L label = null;
    @NonFinal Pair<Option<T>, Option<R>> value = null;
    @NonFinal Iterable<Node<L, Pair<Option<T>, Option<R>>>> children = iterable();

    public static <L, T, R> LooseTwoZipTraversalHandler<L, T, R> usingLooseZipWith(Node<L, R> other) {
        return new LooseTwoZipTraversalHandler<>(other);
    }

    public LooseTwoZipTraversalHandler(Node<L, R> other) {
        this.other = other;
    }

    @Override public void handleSelf(Node<L, T> self) {
        label = self.getLabel();
        value = tuple(option(self.getValue()), option(other.getValue()));
    }

    @Override public void handleChildren(Iterable<Node<L, T>> selfChildren) {
        final Iterable<Node<L, R>> secondChildren = other.getChildren();
        final Iterable<Node<L, T>> firstChildren = selfChildren;

        final Map<L, Node<L, T>> firstChildrenByLabels = mapFromPairs(Lazily.index(firstChildren, Node.Mappers.<L>toLabel()));
        final Map<L, Node<L, R>> secondChildrenByLabels = mapFromPairs(Lazily.index(secondChildren, Node.Mappers.<L>toLabel()));

        final Set<L> firstChildrenLabels = firstChildrenByLabels.keySet();
        final Set<L> secondChildrenLabels = secondChildrenByLabels.keySet();

        final Set<L> labelsInBoth = intersection(firstChildrenLabels, secondChildrenLabels);
        final Set<L> labelsInFirstOnly = difference(firstChildrenLabels, secondChildrenLabels);
        final Set<L> labelsInSecondOnly = difference(secondChildrenLabels, firstChildrenLabels);

        final Iterable<Node<L, T>> childrenInFirstOnly = Maps.selectValues(firstChildrenByLabels, labelsInFirstOnly);
        final Iterable<Node<L, R>> childrenInSecondOnly = Maps.selectValues(secondChildrenByLabels, labelsInSecondOnly);

        final UnaryFunction<T, Pair<Option<T>, Option<R>>> firstValueMapper = new UnaryFunction<T, Pair<Option<T>, Option<R>>>() {
            @Override public Pair<Option<T>, Option<R>> call(T value) {
                return tuple(option(value), Option.<R>none());
            }
        };

        final UnaryFunction<R, Pair<Option<T>, Option<R>>> secondValueMapper = new UnaryFunction<R, Pair<Option<T>, Option<R>>>() {
            @Override public Pair<Option<T>, Option<R>> call(R value) {
                return tuple(Option.<T>none(), option(value));
            }
        };

        final UnaryFunction<L, Node<L, Pair<Option<T>, Option<R>>>> intersectionZipper = new UnaryFunction<L, Node<L, Pair<Option<T>, Option<R>>>>() {
            @Override public Node<L, Pair<Option<T>, Option<R>>> call(L label) {
                return firstChildrenByLabels.get(label).zipLoose(secondChildrenByLabels.get(label));
            }
        };

        children = Literals.<Node<L, Pair<Option<T>, Option<R>>>>iterableBuilder()
                .with(Lazily.map(childrenInFirstOnly, Node.Mappers.<L, T, Pair<Option<T>, Option<R>>>mappingValuesWith(firstValueMapper)))
                .with(Lazily.map(labelsInBoth, intersectionZipper))
                .with(Lazily.map(childrenInSecondOnly, Node.Mappers.<L, R, Pair<Option<T>, Option<R>>>mappingValuesWith(secondValueMapper)))
                .build();
    }

    @Override public boolean goDeeper(Node<L, T> self) {
        return false;
    }

    public Node<L, Pair<Option<T>, Option<R>>> getZipped() {
        return Node.node(label, value, children);
    }
}