package org.javafunk.referee.tree.factories.frommap;

import org.javafunk.funk.Literals;
import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.Tree;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.*;
import static org.javafunk.referee.tree.factories.frommap.MapToTree.fromMapToTree;
import static org.javafunk.referee.tree.Node.branchNode;
import static org.javafunk.referee.tree.Tree.tree;

public class MapToTreeTest {
    @Test
    public void constructsTreeOfDepthOneFromSimpleMap() {
        // Given
        Map<Object, Object> map = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePairs("first", 1, "second", 2)
                .build(LinkedHashMap.class);

        Tree<Object, Object> expectedTree = tree(branchNode("$", iterableWith(
                Node.<Object, Object>leafNode("first", 1),
                Node.<Object, Object>leafNode("second", 2))));

        // When
        Tree<Object, Object> actualTree = fromMapToTree().call(map);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void constructsTreeOfDepthOneFromSimpleIterable() {
        // Given
        Iterable<Object> first = Literals.<Object>iterableWith("1", "2", "3");
        Map<Object, Object> map = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePairs("first", first, "second", 2)
                .build(LinkedHashMap.class);

        Tree<Object, Object> expectedTree = tree(branchNode("$", iterableWith(
                branchNode("first", iterableWith(
                        Node.<Object, Object>leafNode(0, "1"),
                        Node.<Object, Object>leafNode(1, "2"),
                        Node.<Object, Object>leafNode(2, "3"))),
                Node.<Object, Object>leafNode("second", 2))));

        // When
        Tree<Object, Object> actualTree = fromMapToTree().call(map);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void constructsTreeOfDepthTwoFromComplexIterable() {
        // Given
        Map<Object, Object> firstElement = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePair("first-element", 1)
                .build();
        Map<Object, Object> secondElement = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePair("second-element", 2)
                .build();
        Iterable<Object> first = Literals.<Object>iterableWith(firstElement, secondElement);
        Map<Object, Object> map = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePairs("first", first, "second", 2)
                .build(LinkedHashMap.class);

        Tree<Object, Object> expectedTree = tree(branchNode("$", iterableWith(
                branchNode("first", iterableWith(
                        branchNode(0, iterableWith(Node.<Object, Object>leafNode("first-element", 1))),
                        branchNode(1, iterableWith(Node.<Object, Object>leafNode("second-element", 2))))),
                Node.<Object, Object>leafNode("second", 2))));

        // When
        Tree<Object, Object> actualTree = fromMapToTree().call(map);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void constructsTreeOfArbitraryDepthFromIterableOfNestedMaps() {
        // Given
        Map<Object, Object> firstElementMap = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePair("first-element-key", "first-value")
                .build();
        Map<Object, Object> secondElementMap = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePair("second-element-key", "second-value")
                .build();
        Map<Object, Object> firstElement = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePair("first-element", firstElementMap)
                .build();
        Map<Object, Object> secondElement = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePair("second-element", secondElementMap)
                .build();
        Iterable<Object> first = Literals.<Object>iterableWith(firstElement, secondElement);
        Map<Object, Object> map = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePairs("first", first, "second", 2)
                .build(LinkedHashMap.class);

        Tree<Object, Object> expectedTree = tree(branchNode("$", iterableWith(
                branchNode("first", iterableWith(
                        branchNode(0, iterableWith(branchNode("first-element",
                                iterableWith(Node.<Object, Object>leafNode("first-element-key", "first-value"))))),
                        branchNode(1, iterableWith(branchNode("second-element",
                                iterableWith(Node.<Object, Object>leafNode("second-element-key", "second-value"))))))),
                Node.<Object, Object>leafNode("second", 2))));

        // When
        Tree<Object, Object> actualTree = fromMapToTree().call(map);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void constructsTreeFromMapWithNestedMapWithKeysOfDifferentType() {
        // Given
        Map<Integer, Integer> valueMap = mapWithKeyValuePairs(1, 10, 2, 20);
        Map<Object, Object> map = mapBuilderOf(Object.class, Object.class)
                .withKeyValuePairs("first", 1, "second", valueMap)
                .build(LinkedHashMap.class);

        Tree<Object, Object> expectedTree = tree(branchNode("$", iterableWith(
                Node.<Object, Object>leafNode("first", 1),
                branchNode("second", iterableWith(
                        Node.<Object, Object>leafNode(1, 10),
                        Node.<Object, Object>leafNode(2, 20))))));

        // When
        Tree<Object, Object> actualTree = fromMapToTree().call(map);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void constructsTreeOfDepthTwoFromMapOfMaps() {
        // Given
        Map<String, Object> first = Literals.<String, Object>mapBuilder()
                .withKeyValuePairs("first-first", 1, "first-second", 2)
                .build(LinkedHashMap.class);
        Map<String, Object> second = Literals.<String, Object>mapBuilder()
                .withKeyValuePairs("second-first", 1, "second-second", 2)
                .build(LinkedHashMap.class);
        Map<Object, Object> map = mapBuilder()
                .withKeyValuePairs("first", first, "second", second)
                .build(LinkedHashMap.class);

        Tree<Object, Object> expectedTree = tree(branchNode("$", iterableWith(
                branchNode("first", iterableWith(
                        Node.<Object, Object>leafNode("first-first", 1),
                        Node.<Object, Object>leafNode("first-second", 2))),
                branchNode("second", iterableWith(
                        Node.<Object, Object>leafNode("second-first", 1),
                        Node.<Object, Object>leafNode("second-second", 2))))));

        // When
        Tree<Object, Object> actualTree = fromMapToTree().call(map);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void constructsTreeOfArbitraryDepthFromNestedMaps() {
        // Given
        Map<String, Object> firstLeftDown = Literals.<String, Object>mapBuilder()
                .withKeyValuePairs("first-first-left-1", 1, "first-first-left-2", 2)
                .build(LinkedHashMap.class);
        Map<String, Object> firstLeft = Literals.<String, Object>mapBuilder()
                .withKeyValuePair("first-first-left", firstLeftDown)
                .build(LinkedHashMap.class);
        Map<String, Object> first = Literals.<String, Object>mapBuilder()
                .withKeyValuePairs("first-first", firstLeft, "first-second", 2)
                .build(LinkedHashMap.class);
        Map<String, Object> second = Literals.<String, Object>mapBuilder()
                .withKeyValuePairs("second-first", 1, "second-second", 2)
                .build(LinkedHashMap.class);
        Map<Object, Object> map = mapBuilder()
                .withKeyValuePairs("first", first, "second", second)
                .build(LinkedHashMap.class);

        Tree<Object, Object> expectedTree = tree(branchNode("$", iterableWith(
                branchNode("first", iterableWith(
                        branchNode("first-first", iterableWith(
                                branchNode("first-first-left", iterableWith(
                                        Node.<Object, Object>leafNode("first-first-left-1", 1),
                                        Node.<Object, Object>leafNode("first-first-left-2", 2))))),
                        Node.<Object, Object>leafNode("first-second", 2))),
                branchNode("second", iterableWith(
                        Node.<Object, Object>leafNode("second-first", 1),
                        Node.<Object, Object>leafNode("second-second", 2))))));

        // When
        Tree<Object, Object> actualTree = fromMapToTree().call(map);

        // Then
        assertThat(actualTree, is(expectedTree));
    }
}