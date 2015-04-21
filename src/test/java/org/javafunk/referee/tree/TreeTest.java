package org.javafunk.referee.tree;

import lombok.Getter;
import org.javafunk.funk.functors.Mapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.matchbox.Matchers.hasOnlyItemsInOrder;
import static org.javafunk.referee.tree.Node.node;
import static org.javafunk.referee.tree.Traversal.BreadthFirst;
import static org.javafunk.referee.tree.Traversal.DepthFirst;
import static org.javafunk.referee.tree.TreeTest.ValueCollectingVisitor.collectingValuesOf;

public class TreeTest {
    @Test
    public void visitsAllNodesDepthFirst() {
        // Given
        Node<String, Integer> firstGreatGrandchild = Node.leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = Node.leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = Node.leafNode("second-grandchild", 121);
        Node<String, Integer> fourthGrandchild = Node.leafNode("second-grandchild", 122);
        Node<String, Integer> firstChild = node("first-child", 11, iterableWith(firstGrandchild, secondGrandchild));
        Node<String, Integer> secondChild = node("second-child", 12, iterableWith(thirdGrandchild, fourthGrandchild));
        Node<String, Integer> rootNode = node("root", 1, iterableWith(firstChild, secondChild));

        Tree<String, Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visit(DepthFirst, collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1, 11, 111, 1111, 112, 12, 121, 122));
    }

    @Test
    public void visitsAllNodesBreadthFirst() {
        // Given
        Node<String, Integer> firstGreatGrandchild = Node.leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = Node.leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = Node.leafNode("second-grandchild", 121);
        Node<String, Integer> fourthGrandchild = Node.leafNode("second-grandchild", 122);
        Node<String, Integer> firstChild = node("first-child", 11, iterableWith(firstGrandchild, secondGrandchild));
        Node<String, Integer> secondChild = node("second-child", 12, iterableWith(thirdGrandchild, fourthGrandchild));
        Node<String, Integer> rootNode = node("root", 1, iterableWith(firstChild, secondChild));

        Tree<String, Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visit(BreadthFirst, collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1, 11, 12, 111, 112, 121, 122, 1111));
    }

    @Test
    public void mapsNodeValuesUsingSuppliedMapper() {
        // Given
        Node<String, Integer> firstIntegerGrandchild = Node.leafNode("first-grandchild", 111);
        Node<String, Integer> secondIntegerGrandchild = Node.leafNode("second-grandchild", 112);
        Node<String, Integer> thirdIntegerGrandchild = Node.leafNode("second-grandchild", 121);
        Node<String, Integer> fourthIntegerGrandchild = Node.leafNode("second-grandchild", 122);
        Node<String, Integer> firstIntegerChild = node("first-child", 11, iterableWith(firstIntegerGrandchild, secondIntegerGrandchild));
        Node<String, Integer> secondIntegerChild = node("second-child", 12, iterableWith(thirdIntegerGrandchild, fourthIntegerGrandchild));
        Node<String, Integer> rootIntegerNode = node("root", 1, iterableWith(firstIntegerChild, secondIntegerChild));

        Tree<String, Integer> initial = new Tree<>(rootIntegerNode);

        Node<String, String> firstStringGrandchild = Node.leafNode("first-grandchild", "111");
        Node<String, String> secondStringGrandchild = Node.leafNode("second-grandchild", "112");
        Node<String, String> thirdStringGrandchild = Node.leafNode("second-grandchild", "121");
        Node<String, String> fourthStringGrandchild = Node.leafNode("second-grandchild", "122");
        Node<String, String> firstStringChild = node("first-child", "11", iterableWith(firstStringGrandchild, secondStringGrandchild));
        Node<String, String> secondStringChild = node("second-child", "12", iterableWith(thirdStringGrandchild, fourthStringGrandchild));
        Node<String, String> rootStringNode = node("root", "1", iterableWith(firstStringChild, secondStringChild));

        Tree<String, String> expected = new Tree<>(rootStringNode);

        // When
        Tree<String, String> actual = initial.mapValue(new Mapper<Integer, String>() {
            @Override public String map(Integer input) {
                return String.valueOf(input);
            }
        });

        // Then
        assertThat(actual, is(expected));
    }

    public static class ValueCollectingVisitor<T> implements Visitor<String, T, ValueCollectingVisitor<T>> {
        @Getter List<T> values = new ArrayList<>();

        public static <S> ValueCollectingVisitor<S> collectingValuesOf(Class<S> klass) {
            return new ValueCollectingVisitor<>();
        }

        @Override public ValueCollectingVisitor<T> visit(Node<String, T> node) {
            values.add(node.getValue());
            return this;
        }
    }
}