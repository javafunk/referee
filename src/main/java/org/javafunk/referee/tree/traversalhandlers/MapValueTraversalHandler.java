package org.javafunk.referee.tree.traversalhandlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.tree.Node;

import static org.javafunk.funk.Literals.iterable;
import static org.javafunk.funk.Literals.iterableBuilderFrom;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapValueTraversalHandler<L, T, R>
        extends NoOpTraversalHandler<L, T> {
    UnaryFunction<T, R> mapper;
    @NonFinal L label = null;
    @NonFinal R value = null;
    @NonFinal Iterable<Node<L, R>> children = iterable();

    public static <L, T, R> MapValueTraversalHandler<L, T, R> mappingValueWith(UnaryFunction<T, R> mapper) {
        return new MapValueTraversalHandler<>(mapper);
    }

    public MapValueTraversalHandler(UnaryFunction<T, R> mapper) {
        this.mapper = mapper;
    }

    @Override public void handleSelf(Node<L, T> self) {
        label = self.getLabel();
        value = mapper.call(self.getValue());
    }

    @Override public void handleChild(Integer index, Node<L, T> child) {
        children = iterableBuilderFrom(children)
                .with(child.mapValue(mapper))
                .build();
    }

    @Override public boolean goDeeper() {
        return false;
    }

    public Node<L, R> getMapped() {
        return Node.node(label, value, children);
    }
}
