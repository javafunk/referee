package org.javafunk.referee.tree.visitors;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.Visitor;

import static org.javafunk.funk.monads.Option.none;
import static org.javafunk.funk.monads.Option.option;

@Value
@AllArgsConstructor
public class FindByLabelVisitor<L, T>
        implements Visitor<L, T, FindByLabelVisitor<L, T>> {
    L label;
    Option<Node<L, T>> node;

    public static <L, T> FindByLabelVisitor<L, T> findByLabel(L label) {
        return new FindByLabelVisitor<L, T>(label);
    }

    public FindByLabelVisitor(L label) {
        this(label, Option.<Node<L, T>>none());
    }

    @Override public FindByLabelVisitor<L, T> visit(Node<L, T> node) {
        if (node.hasLabel(label)) {
            return new FindByLabelVisitor<>(label, option(node));
        }
        return this;
    }
}
