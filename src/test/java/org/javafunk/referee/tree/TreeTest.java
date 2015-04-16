package org.javafunk.referee.tree;

import lombok.Getter;
import org.javafunk.funk.functors.Mapper;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.Problem;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.matchbox.Matchers.hasOnlyItemsInOrder;
import static org.javafunk.referee.tree.TreeTest.ValueCollectingVisitor.collectingValuesOf;

public class TreeTest {
    @Test
    public void visitsAllNodesDepthFirst() {
        // Given
        Node<Integer> firstGrandchild = new Node<>("first-grandchild", 111);
        Node<Integer> secondGrandchild = new Node<>("second-grandchild", 112);
        Node<Integer> thirdGrandchild = new Node<>("second-grandchild", 121);
        Node<Integer> fourthGrandchild = new Node<>("second-grandchild", 122);
        Node<Integer> firstChild = new Node<>("first-child", 11, iterableWith(firstGrandchild, secondGrandchild));
        Node<Integer> secondChild = new Node<>("second-child", 12, iterableWith(thirdGrandchild, fourthGrandchild));
        Node<Integer> rootNode = new Node<>("root", 1, iterableWith(firstChild, secondChild));

        Tree<Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visitDepthFirst(collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1, 11, 111, 112, 12, 121, 122));
    }

    @Test
    public void mapsNodeValuesUsingSuppliedMapper() {
        // Given
        Node<Integer> firstIntegerGrandchild = new Node<>("first-grandchild", 111);
        Node<Integer> secondIntegerGrandchild = new Node<>("second-grandchild", 112);
        Node<Integer> thirdIntegerGrandchild = new Node<>("second-grandchild", 121);
        Node<Integer> fourthIntegerGrandchild = new Node<>("second-grandchild", 122);
        Node<Integer> firstIntegerChild = new Node<>("first-child", 11, iterableWith(firstIntegerGrandchild, secondIntegerGrandchild));
        Node<Integer> secondIntegerChild = new Node<>("second-child", 12, iterableWith(thirdIntegerGrandchild, fourthIntegerGrandchild));
        Node<Integer> rootIntegerNode = new Node<>("root", 1, iterableWith(firstIntegerChild, secondIntegerChild));

        Tree<Integer> initial = new Tree<>(rootIntegerNode);

        Node<String> firstStringGrandchild = new Node<>("first-grandchild", "111");
        Node<String> secondStringGrandchild = new Node<>("second-grandchild", "112");
        Node<String> thirdStringGrandchild = new Node<>("second-grandchild", "121");
        Node<String> fourthStringGrandchild = new Node<>("second-grandchild", "122");
        Node<String> firstStringChild = new Node<>("first-child", "11", iterableWith(firstStringGrandchild, secondStringGrandchild));
        Node<String> secondStringChild = new Node<>("second-child", "12", iterableWith(thirdStringGrandchild, fourthStringGrandchild));
        Node<String> rootStringNode = new Node<>("root", "1", iterableWith(firstStringChild, secondStringChild));

        Tree<String> expected = new Tree<>(rootStringNode);

        // When
        Tree<String> actual = initial.mapValueDepthFirst(new Mapper<Integer, String>() {
            @Override public String map(Integer input) {
                return String.valueOf(input);
            }
        });

        // Then
        assertThat(actual, is(expected));
    }

    public static class ValueCollectingVisitor<T> implements Visitor<T> {
        @Getter List<T> values = new ArrayList<>();

        public static <S> ValueCollectingVisitor<S> collectingValuesOf(Class<S> klass) {
            return new ValueCollectingVisitor<>();
        }

        @Override public Visitor<T> visit(Node<T> node) {
            values.add(node.getValue());
            return this;
        }
    }
}