package org.javafunk.referee;

import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.tree.Tree;

import java.util.Map;

import static org.javafunk.referee.MapToNode.fromMapToNode;
import static org.javafunk.referee.tree.Tree.tree;

public class MapToTree implements UnaryFunction<Map<Object, Object>, Tree<Object, Object>> {
    public static MapToTree fromMapToTree() {
        return new MapToTree();
    }

    @Override public Tree<Object, Object> call(Map<Object, Object> map) {
        return tree(fromMapToNode().call(map));
    }
}
