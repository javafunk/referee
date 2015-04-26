package org.javafunk.referee.tree.traversalhandlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.tree.Node;

import static org.javafunk.funk.Literals.iterable;
import static org.javafunk.funk.Literals.iterableBuilderFrom;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapLabelTraversalHandler<L, T, M>
        extends NoOpTraversalHandler<L, T> {
    UnaryFunction<L, M> mapper;
    @NonFinal M label = null;
    @NonFinal T value = null;
    @NonFinal Iterable<Node<M, T>> children = iterable();

    public static <L, T, M> MapLabelTraversalHandler<L, T, M> mappingLabelWith(UnaryFunction<L, M> mapper) {
        return new MapLabelTraversalHandler<>(mapper);
    }

    public MapLabelTraversalHandler(UnaryFunction<L, M> mapper) {
        this.mapper = mapper;
    }

    @Override public void handleSelf(Node<L, T> self) {
        label = mapper.call(self.getLabel());
        value = self.getValue();
    }

    @Override public void handleChild(Integer index, Node<L, T> child) {
        children = iterableBuilderFrom(children)
                .with(child.mapLabels(mapper))
                .build();
    }

    @Override public boolean goDeeper() {
        return false;
    }

    public Node<M, T> getMapped() {
        return Node.node(label, value, children);
    }
}
