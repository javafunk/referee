package org.javafunk.referee.tree;

import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.monads.Option;

public enum ZipMode {
    Strict {
        @Override public <L, T, R> Node<L, Pair<Option<T>, Option<R>>> zip(Node<L, T> first, Node<L, R> second) {
            return first.zipStrict(second);
        }
    },
    Loose {
        @Override public <L, T, R> Node<L, Pair<Option<T>, Option<R>>> zip(Node<L, T> first, Node<L, R> second) {
            return first.zipLoose(second);
        }
    };

    public abstract <L, T, R> Node<L,Pair<Option<T>,Option<R>>> zip(Node<L, T> first, Node<L, R> second);
}
