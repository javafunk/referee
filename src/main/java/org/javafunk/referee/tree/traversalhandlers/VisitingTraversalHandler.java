package org.javafunk.referee.tree.traversalhandlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.javafunk.referee.tree.TraversalHandler;
import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.Visitor;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VisitingTraversalHandler<L, T, S extends Visitor<L, T, S>>
        extends NoOpTraversalHandler<L, T> {
    @Getter S visitor;

    public static <L, T, S  extends Visitor<L, T, S>> VisitingTraversalHandler<L, T, S> usingVisitor(S visitor) {
        return new VisitingTraversalHandler<>(visitor);
    }

    @Override public void handleSelf(Node<L, T> self) {
        visitor = visitor.visit(self);
    }
}
