package org.javafunk.referee.tree;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.Literals;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.Action;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.functors.predicates.UnaryPredicate;
import org.javafunk.funk.functors.procedures.UnaryProcedure;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.tree.traversalhandlers.*;
import org.javafunk.referee.tree.visitors.FindByLabelVisitor;

import java.util.LinkedList;
import java.util.Queue;

import static org.javafunk.funk.Literals.collectionWith;
import static org.javafunk.referee.tree.Traversal.DepthFirstPreOrder;
import static org.javafunk.referee.tree.traversalhandlers.VisitingTraversalHandler.usingVisitor;

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


    public boolean hasLabel(L label) {
        return this.label.equals(label);
    }

    public <H extends TraversalHandler<L, T>> H traverse(H traversalHandler) {
        return DepthFirstPreOrder.traverse(this, traversalHandler);
    }

    public <H extends TraversalHandler<L, T>> H traverse(Traversal traversal, H traversalHandler) {
        return traversal.traverse(this, traversalHandler);
    }

    public <H extends TraversalHandler<L, T>> H traverseDepthFirstPreOrder(final H traversalHandler) {
        traversalHandler.handleSelf(this);
        traversalHandler.handleChildren(children);

        Eagerly.each(Lazily.enumerate(children), new UnaryProcedure<Pair<Integer, Node<L, T>>>() {
            @Override public void execute(Pair<Integer, Node<L, T>> child) {
                final Integer index = child.getFirst();
                final Node<L, T> node = child.getSecond();

                traversalHandler.handleChild(index, node);
                if (traversalHandler.goDeeper()) {
                    node.traverseDepthFirstPreOrder(traversalHandler);
                }
            }
        });

        return traversalHandler;
    }

    public <H extends TraversalHandler<L, T>> H traverseDepthFirstPostOrder(final H traversalHandler) {
        Eagerly.each(Lazily.enumerate(children), new UnaryProcedure<Pair<Integer, Node<L, T>>>() {
            @Override public void execute(Pair<Integer, Node<L, T>> child) {
                final Integer index = child.getFirst();
                final Node<L, T> node = child.getSecond();

                if (traversalHandler.goDeeper()) {
                    node.traverseDepthFirstPostOrder(traversalHandler);
                }
                traversalHandler.handleChild(index, node);
            }
        });

        traversalHandler.handleSelf(this);
        traversalHandler.handleChildren(children);

        return traversalHandler;
    }

    public <H extends TraversalHandler<L, T>> H traverseBreadthFirstLeftToRight(final H traversalHandler) {
        final Queue<Node<L, T>> nodeQueue = new LinkedList<>(collectionWith(this));

        while (!nodeQueue.isEmpty()) {
            Node<L, T> node = nodeQueue.remove();
            traversalHandler.handleSelf(node);
            traversalHandler.handleChildren(children);

            Eagerly.each(Lazily.enumerate(node.getChildren()), new Action<Pair<Integer, Node<L, T>>>() {
                @Override public void on(Pair<Integer, Node<L, T>> child) {
                    final Integer index = child.getFirst();
                    final Node<L, T> node = child.getSecond();

                    traversalHandler.handleChild(index, node);
                    if (traversalHandler.goDeeper()) {
                        nodeQueue.add(node);
                    }
                }
            });
        }

        return traversalHandler;
    }

    public <H extends TraversalHandler<L, T>> H traverseBreadthFirstRightToLeft(final H traversalHandler) {
        final Queue<Node<L, T>> nodeQueue = new LinkedList<>(collectionWith(this));

        while (!nodeQueue.isEmpty()) {
            Node<L, T> node = nodeQueue.remove();
            traversalHandler.handleSelf(node);
            traversalHandler.handleChildren(children);

            Eagerly.each(Eagerly.reverse(Lazily.enumerate(node.getChildren())), new Action<Pair<Integer, Node<L, T>>>() {
                @Override public void on(Pair<Integer, Node<L, T>> child) {
                    final Integer index = child.getFirst();
                    final Node<L, T> node = child.getSecond();

                    traversalHandler.handleChild(index, node);
                    if (traversalHandler.goDeeper()) {
                        nodeQueue.add(node);
                    }
                }
            });
        }

        return traversalHandler;
    }

    public <S extends Visitor<L, T, S>> S visit(S visitor) {
        return DepthFirstPreOrder.visit(this, visitor);
    }

    public <S extends Visitor<L, T, S>> S visit(Traversal traversal, S visitor) {
        return traversal.visit(this, visitor);
    }

    public <S extends Visitor<L, T, S>> S visitDepthFirstPreOrder(final S visitor) {
        return traverseDepthFirstPreOrder(usingVisitor(visitor)).getVisitor();
    }

    public <S extends Visitor<L, T, S>> S visitDepthFirstPostOrder(S visitor) {
        return traverseDepthFirstPostOrder(usingVisitor(visitor)).getVisitor();
    }

    public <S extends Visitor<L, T, S>> S visitBreadthFirstLeftToRight(S visitor) {
        return traverseBreadthFirstLeftToRight(usingVisitor(visitor)).getVisitor();
    }

    public <S extends Visitor<L, T, S>> S visitBreadthFirstRightToLeft(S visitor) {
        return traverseBreadthFirstRightToLeft(usingVisitor(visitor)).getVisitor();
    }

    public <R> Node<L, Pair<T, R>> zip(Node<L, R> otherNode) {
        return traverseBreadthFirstLeftToRight(TwoZipTraversalHandler.<L, T, R>usingZipWith(otherNode)).getZipped();
    }

    public <R> Node<L, Pair<Option<T>, Option<R>>> zip(ZipMode zipMode, Node<L, R> otherNode) {
        return zipMode.zip(this, otherNode);
    }

    public <R> Node<L, Pair<Option<T>, Option<R>>> zipStrict(Node<L, R> otherNode) {
        return traverseBreadthFirstLeftToRight(StrictTwoZipTraversalHandler.<L, T, R>usingStrictZipWith(otherNode)).getZipped();
    }

    public <R> Node<L, Pair<Option<T>, Option<R>>> zipLoose(Node<L, R> otherNode) {
        return traverseBreadthFirstLeftToRight(LooseTwoZipTraversalHandler.<L, T, R>usingLooseZipWith(otherNode)).getZipped();
    }

    public <R> Node<L, R> mapValues(UnaryFunction<T, R> valueMapper) {
        return traverseDepthFirstPreOrder(MapValueTraversalHandler.<L, T, R>mappingValueWith(valueMapper)).getMapped();
    }

    public <M> Node<M, T> mapLabels(UnaryFunction<L, M> labelMapper) {
        return traverseDepthFirstPreOrder(MapLabelTraversalHandler.<L, T, M>mappingLabelWith(labelMapper)).getMapped();
    }

    public Option<Node<L, T>> findChildBy(L label) {
        return Eagerly.firstMatching(children, Predicates.<L, T>havingLabel(label));
    }

    public Option<Node<L, T>> findDescendantBy(L label) {
        return visitDepthFirstPreOrder(FindByLabelVisitor.<L, T>findByLabel(label)).getNode();
    }

    public static class Predicates {
        private Predicates() {}

        public static <L, T> UnaryPredicate<Node<L, T>> havingLabel(final L label) {
            return new UnaryPredicate<Node<L, T>>() {
                @Override public boolean evaluate(Node<L, T> node) {
                    return node.hasLabel(label);
                }
            };
        }
    }

    public static class Mappers {
        private Mappers() {}

        public static <L> UnaryFunction<Node<L, ?>, L> toLabel() {
            return new UnaryFunction<Node<L, ?>, L>() {
                @Override public L call(Node<L, ?> node) {
                    return node.getLabel();
                }
            };
        }

        public static <L, T, R> UnaryFunction<Node<L, T>, Node<L, R>> mappingValuesWith(final UnaryFunction<T, R> mapper) {
            return new UnaryFunction<Node<L, T>, Node<L, R>>() {
                @Override public Node<L, R> call(Node<L, T> node) {
                    return node.mapValues(mapper);
                }
            };
        }
    }
}
