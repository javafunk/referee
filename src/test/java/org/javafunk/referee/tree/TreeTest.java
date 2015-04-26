package org.javafunk.referee.tree;

import lombok.Getter;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.Mapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.funk.Literals.tuple;
import static org.javafunk.matchbox.Matchers.hasOnlyItemsInOrder;
import static org.javafunk.referee.tree.Node.node;
import static org.javafunk.referee.tree.Traversal.*;
import static org.javafunk.referee.tree.TreeTest.ValueCollectingVisitor.collectingValuesOf;

public class TreeTest {
    @Test
    public void visitsAllNodesUsingDepthFirstPreOrder() {
        // Given
        Node<String, Integer> firstGreatGrandchild = Node.leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> secondGreatGrandchild = Node.leafNode("second-greatgrandchild", 1131);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = Node.leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = Node.node("third-grandchild", 113, iterableWith(secondGreatGrandchild));
        Node<String, Integer> fourthGrandchild = Node.leafNode("fourth-grandchild", 121);
        Node<String, Integer> fifthGrandchild = Node.leafNode("fifth-grandchild", 122);
        Node<String, Integer> firstChild = node("first-child", 11, iterableWith(firstGrandchild, secondGrandchild, thirdGrandchild));
        Node<String, Integer> secondChild = node("second-child", 12, iterableWith(fourthGrandchild, fifthGrandchild));
        Node<String, Integer> rootNode = node("root", 1, iterableWith(firstChild, secondChild));

        Tree<String, Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visit(DepthFirstPreOrder, collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1, 11, 111, 1111, 112, 113, 1131, 12, 121, 122));
    }

    @Test
    public void visitsAllNodesUsingDepthFirstPostOrderTraversal() {
        // Given
        Node<String, Integer> firstGreatGrandchild = Node.leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> secondGreatGrandchild = Node.leafNode("second-greatgrandchild", 1131);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = Node.leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = Node.node("third-grandchild", 113, iterableWith(secondGreatGrandchild));
        Node<String, Integer> fourthGrandchild = Node.leafNode("fourth-grandchild", 121);
        Node<String, Integer> fifthGrandchild = Node.leafNode("fifth-grandchild", 122);
        Node<String, Integer> firstChild = node("first-child", 11, iterableWith(firstGrandchild, secondGrandchild, thirdGrandchild));
        Node<String, Integer> secondChild = node("second-child", 12, iterableWith(fourthGrandchild, fifthGrandchild));
        Node<String, Integer> rootNode = node("root", 1, iterableWith(firstChild, secondChild));

        Tree<String, Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visit(DepthFirstPostOrder, collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1111, 111, 112, 1131, 113, 11, 121, 122, 12, 1));
    }

    @Test
    public void visitsAllNodesUsingBreadthFirstLeftToRightTraversal() {
        // Given
        Node<String, Integer> firstGreatGrandchild = Node.leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = Node.leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = Node.leafNode("third-grandchild", 113);
        Node<String, Integer> fourthGrandchild = Node.leafNode("fourth-grandchild", 121);
        Node<String, Integer> fifthGrandchild = Node.leafNode("fifth-grandchild", 122);
        Node<String, Integer> firstChild = node("first-child", 11, iterableWith(firstGrandchild, secondGrandchild, thirdGrandchild));
        Node<String, Integer> secondChild = node("second-child", 12, iterableWith(fourthGrandchild, fifthGrandchild));
        Node<String, Integer> rootNode = node("root", 1, iterableWith(firstChild, secondChild));

        Tree<String, Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visit(BreadthFirstLeftToRight, collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1, 11, 12, 111, 112, 113, 121, 122, 1111));
    }

    @Test
    public void visitsAllNodesUsingBreadthFirstRightToLeftTraversal() {
        // Given
        Node<String, Integer> firstGreatGrandchild = Node.leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = Node.leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = Node.leafNode("third-grandchild", 113);
        Node<String, Integer> fourthGrandchild = Node.leafNode("fourth-grandchild", 121);
        Node<String, Integer> fifthGrandchild = Node.leafNode("fifth-grandchild", 122);
        Node<String, Integer> firstChild = node("first-child", 11, iterableWith(firstGrandchild, secondGrandchild, thirdGrandchild));
        Node<String, Integer> secondChild = node("second-child", 12, iterableWith(fourthGrandchild, fifthGrandchild));
        Node<String, Integer> rootNode = node("root", 1, iterableWith(firstChild, secondChild));

        Tree<String, Integer> tree = new Tree<>(rootNode);

        // When
        ValueCollectingVisitor<Integer> visitor = tree.visit(BreadthFirstRightToLeft, collectingValuesOf(Integer.class));

        // Then
        assertThat(visitor.getValues(), hasOnlyItemsInOrder(
                1, 12, 11, 122, 121, 113, 112, 111, 1111));
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
    
    @Test
    public void zipsTwoTreesTogetherByLabelWhenIdenticalInStructure() {
        // Given
        Node<String, Integer> firstNode111 = Node.leafNode("111", 111);
        Node<String, Integer> firstNode112 = Node.leafNode("112", 112);
        Node<String, Integer> firstNode113 = Node.leafNode("113", 113);
        Node<String, Integer> firstNode121 = Node.leafNode("121", 121);
        Node<String, Integer> firstNode122 = Node.leafNode("122", 122);
        Node<String, Integer> firstNode11 = node("11", 11, iterableWith(firstNode111, firstNode112, firstNode113));
        Node<String, Integer> firstNode12 = node("12", 12, iterableWith(firstNode121, firstNode122));
        Node<String, Integer> firstNode1 = node("1", 1, iterableWith(firstNode11, firstNode12));

        Node<String, String> secondNode111 = Node.leafNode("111", "111");
        Node<String, String> secondNode112 = Node.leafNode("112", "112");
        Node<String, String> secondNode113 = Node.leafNode("113", "113");
        Node<String, String> secondNode121 = Node.leafNode("121", "121");
        Node<String, String> secondNode122 = Node.leafNode("122", "122");
        Node<String, String> secondNode11 = node("11", "11", iterableWith(secondNode111, secondNode112, secondNode113));
        Node<String, String> secondNode12 = node("12", "12", iterableWith(secondNode121, secondNode122));
        Node<String, String> secondNode1 = node("1", "1", iterableWith(secondNode11, secondNode12));

        Tree<String, Integer> firstTree = new Tree<>(firstNode1);
        Tree<String, String> secondTree = new Tree<>(secondNode1);

        Node<String, Pair<Integer, String>> expectedNode111 = Node.leafNode("111", tuple(111, "111"));
        Node<String, Pair<Integer, String>> expectedNode112 = Node.leafNode("112", tuple(112, "112"));
        Node<String, Pair<Integer, String>> expectedNode113 = Node.leafNode("113", tuple(113, "113"));
        Node<String, Pair<Integer, String>> expectedNode121 = Node.leafNode("121", tuple(121, "121"));
        Node<String, Pair<Integer, String>> expectedNode122 = Node.leafNode("122", tuple(122, "122"));
        Node<String, Pair<Integer, String>> expectedNode11 = node("11", tuple(11, "11"), iterableWith(expectedNode111, expectedNode112, expectedNode113));
        Node<String, Pair<Integer, String>> expectedNode12 = node("12", tuple(12, "12"), iterableWith(expectedNode121, expectedNode122));
        Node<String, Pair<Integer, String>> expectedNode1 = node("1", tuple(1, "1"), iterableWith(expectedNode11, expectedNode12));

        Tree<String, Pair<Integer, String>> expectedTree = new Tree<>(expectedNode1);

        // When
        Tree<String, Pair<Integer, String>> actualTree = firstTree.zip(secondTree);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void zipsTwoTreesTogetherByLabelIgnoringNodesWithoutACorrespondingNode() {
        // Given
        Node<String, Integer> firstNode111 = Node.leafNode("111", 111);
        Node<String, Integer> firstNode112 = Node.leafNode("112", 112);
        Node<String, Integer> firstNode113 = Node.leafNode("113", 113);
        Node<String, Integer> firstNode121 = Node.leafNode("121", 121);
        Node<String, Integer> firstNode122 = Node.leafNode("122", 122);
        Node<String, Integer> firstNode11 = node("11", 11, iterableWith(firstNode111, firstNode112, firstNode113));
        Node<String, Integer> firstNode12 = node("12", 12, iterableWith(firstNode121, firstNode122));
        Node<String, Integer> firstNode1 = node("1", 1, iterableWith(firstNode11, firstNode12));

        Node<String, String> secondNode111 = Node.leafNode("111", "111");
        Node<String, String> secondNode113 = Node.leafNode("113", "113");
        Node<String, String> secondNode11 = node("11", "11", iterableWith(secondNode111, secondNode113));
        Node<String, String> secondNode1 = node("1", "1", iterableWith(secondNode11));

        Tree<String, Integer> firstTree = new Tree<>(firstNode1);
        Tree<String, String> secondTree = new Tree<>(secondNode1);

        Node<String, Pair<Integer, String>> expectedNode111 = Node.leafNode("111", tuple(111, "111"));
        Node<String, Pair<Integer, String>> expectedNode113 = Node.leafNode("113", tuple(113, "113"));
        Node<String, Pair<Integer, String>> expectedNode11 = node("11", tuple(11, "11"), iterableWith(expectedNode111, expectedNode113));
        Node<String, Pair<Integer, String>> expectedNode1 = node("1", tuple(1, "1"), iterableWith(expectedNode11));

        Tree<String, Pair<Integer, String>> expectedTree = new Tree<>(expectedNode1);

        // When
        Tree<String, Pair<Integer, String>> actualTree = firstTree.zip(secondTree);

        // Then
        assertThat(actualTree, is(expectedTree));
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