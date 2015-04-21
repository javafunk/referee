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
        Node<Integer> firstGreatGrandchild = node("first-greatgrandchild", 1111);
        Node<Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<Integer> secondGrandchild = node("second-grandchild", 112);
        Node<Integer> thirdGrandchild = node("second-grandchild", 121);
        Node<Integer> fourthGrandchild = node("second-grandchild", 122);
        Node<Integer> firstChild = node("first-child", 11, iterableWith(firstGrandchild, secondGrandchild));
        Node<Integer> secondChild = node("second-child", 12, iterableWith(thirdGrandchild, fourthGrandchild));
        Node<Integer> rootNode = node("root", 1, iterableWith(firstChild, secondChild));

        Tree<Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visit(DepthFirst, collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1, 11, 111, 1111, 112, 12, 121, 122));
    }

    @Test
    public void visitsAllNodesBreadthFirst() {
        // Given
        Node<Integer> firstGreatGrandchild = node("first-greatgrandchild", 1111);
        Node<Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<Integer> secondGrandchild = node("second-grandchild", 112);
        Node<Integer> thirdGrandchild = node("second-grandchild", 121);
        Node<Integer> fourthGrandchild = node("second-grandchild", 122);
        Node<Integer> firstChild = node("first-child", 11, iterableWith(firstGrandchild, secondGrandchild));
        Node<Integer> secondChild = node("second-child", 12, iterableWith(thirdGrandchild, fourthGrandchild));
        Node<Integer> rootNode = node("root", 1, iterableWith(firstChild, secondChild));

        Tree<Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visit(BreadthFirst, collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1, 11, 12, 111, 112, 121, 122, 1111));
    }

    @Test
    public void mapsNodeValuesUsingSuppliedMapper() {
        // Given
        Node<Integer> firstIntegerGrandchild = node("first-grandchild", 111);
        Node<Integer> secondIntegerGrandchild = node("second-grandchild", 112);
        Node<Integer> thirdIntegerGrandchild = node("second-grandchild", 121);
        Node<Integer> fourthIntegerGrandchild = node("second-grandchild", 122);
        Node<Integer> firstIntegerChild = node("first-child", 11, iterableWith(firstIntegerGrandchild, secondIntegerGrandchild));
        Node<Integer> secondIntegerChild = node("second-child", 12, iterableWith(thirdIntegerGrandchild, fourthIntegerGrandchild));
        Node<Integer> rootIntegerNode = node("root", 1, iterableWith(firstIntegerChild, secondIntegerChild));

        Tree<Integer> initial = new Tree<>(rootIntegerNode);

        Node<String> firstStringGrandchild = node("first-grandchild", "111");
        Node<String> secondStringGrandchild = node("second-grandchild", "112");
        Node<String> thirdStringGrandchild = node("second-grandchild", "121");
        Node<String> fourthStringGrandchild = node("second-grandchild", "122");
        Node<String> firstStringChild = node("first-child", "11", iterableWith(firstStringGrandchild, secondStringGrandchild));
        Node<String> secondStringChild = node("second-child", "12", iterableWith(thirdStringGrandchild, fourthStringGrandchild));
        Node<String> rootStringNode = node("root", "1", iterableWith(firstStringChild, secondStringChild));

        Tree<String> expected = new Tree<>(rootStringNode);

        // When
        Tree<String> actual = initial.mapValue(new Mapper<Integer, String>() {
            @Override public String map(Integer input) {
                return String.valueOf(input);
            }
        });

        // Then
        assertThat(actual, is(expected));
    }

    public static class ValueCollectingVisitor<T> implements Visitor<T, ValueCollectingVisitor<T>> {
        @Getter List<T> values = new ArrayList<>();

        public static <S> ValueCollectingVisitor<S> collectingValuesOf(Class<S> klass) {
            return new ValueCollectingVisitor<>();
        }

        @Override public ValueCollectingVisitor<T> visit(Node<T> node) {
            values.add(node.getValue());
            return this;
        }
    }
}