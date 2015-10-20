package org.javafunk.referee.tree;

import lombok.Getter;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.Mapper;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.monads.Option;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.funk.Literals.tuple;
import static org.javafunk.funk.monads.Option.none;
import static org.javafunk.funk.monads.Option.option;
import static org.javafunk.matchbox.Matchers.hasOnlyItemsInOrder;
import static org.javafunk.referee.tree.Node.leafNode;
import static org.javafunk.referee.tree.Node.node;
import static org.javafunk.referee.tree.Traversal.*;
import static org.javafunk.referee.tree.TreeTest.ValueCollectingVisitor.collectingValuesOf;
import static org.javafunk.referee.tree.ZipMode.Loose;
import static org.javafunk.referee.tree.ZipMode.Strict;

public class TreeTest {
    @Test
    public void visitsAllNodesUsingDepthFirstPreOrderTraversal() {
        // Given
        Node<String, Integer> firstGreatGrandchild = leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> secondGreatGrandchild = leafNode("second-greatgrandchild", 1131);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = Node.node("third-grandchild", 113, iterableWith(secondGreatGrandchild));
        Node<String, Integer> fourthGrandchild = leafNode("fourth-grandchild", 121);
        Node<String, Integer> fifthGrandchild = leafNode("fifth-grandchild", 122);
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
        Node<String, Integer> firstGreatGrandchild = leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> secondGreatGrandchild = leafNode("second-greatgrandchild", 1131);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = Node.node("third-grandchild", 113, iterableWith(secondGreatGrandchild));
        Node<String, Integer> fourthGrandchild = leafNode("fourth-grandchild", 121);
        Node<String, Integer> fifthGrandchild = leafNode("fifth-grandchild", 122);
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
        Node<String, Integer> firstGreatGrandchild = leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = leafNode("third-grandchild", 113);
        Node<String, Integer> fourthGrandchild = leafNode("fourth-grandchild", 121);
        Node<String, Integer> fifthGrandchild = leafNode("fifth-grandchild", 122);
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
        Node<String, Integer> firstGreatGrandchild = leafNode("first-greatgrandchild", 1111);
        Node<String, Integer> firstGrandchild = node("first-grandchild", 111, iterableWith(firstGreatGrandchild));
        Node<String, Integer> secondGrandchild = leafNode("second-grandchild", 112);
        Node<String, Integer> thirdGrandchild = leafNode("third-grandchild", 113);
        Node<String, Integer> fourthGrandchild = leafNode("fourth-grandchild", 121);
        Node<String, Integer> fifthGrandchild = leafNode("fifth-grandchild", 122);
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
        Node<String, Integer> firstIntegerGrandchild = leafNode("first-grandchild", 111);
        Node<String, Integer> secondIntegerGrandchild = leafNode("second-grandchild", 112);
        Node<String, Integer> thirdIntegerGrandchild = leafNode("third-grandchild", 121);
        Node<String, Integer> fourthIntegerGrandchild = leafNode("fourth-grandchild", 122);
        Node<String, Integer> firstIntegerChild = node("first-child", 11, iterableWith(firstIntegerGrandchild, secondIntegerGrandchild));
        Node<String, Integer> secondIntegerChild = node("second-child", 12, iterableWith(thirdIntegerGrandchild, fourthIntegerGrandchild));
        Node<String, Integer> rootIntegerNode = node("root", 1, iterableWith(firstIntegerChild, secondIntegerChild));

        Tree<String, Integer> initial = new Tree<>(rootIntegerNode);

        Node<String, String> firstStringGrandchild = leafNode("first-grandchild", "111");
        Node<String, String> secondStringGrandchild = leafNode("second-grandchild", "112");
        Node<String, String> thirdStringGrandchild = leafNode("third-grandchild", "121");
        Node<String, String> fourthStringGrandchild = leafNode("fourth-grandchild", "122");
        Node<String, String> firstStringChild = node("first-child", "11", iterableWith(firstStringGrandchild, secondStringGrandchild));
        Node<String, String> secondStringChild = node("second-child", "12", iterableWith(thirdStringGrandchild, fourthStringGrandchild));
        Node<String, String> rootStringNode = node("root", "1", iterableWith(firstStringChild, secondStringChild));

        Tree<String, String> expected = new Tree<>(rootStringNode);

        // When
        Tree<String, String> actual = initial.mapValues(new Mapper<Integer, String>() {
            @Override public String map(Integer input) {
                return String.valueOf(input);
            }
        });

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void mapsNodeLabelsUsingSuppliedMapper() {
        // Given
        Node<String, Integer> firstInitialGrandchild = leafNode("first-grandchild", 111);
        Node<String, Integer> secondInitialGrandchild = leafNode("second-grandchild", 112);
        Node<String, Integer> thirdInitialGrandchild = leafNode("third-grandchild", 121);
        Node<String, Integer> fourthInitialGrandchild = leafNode("fourth-grandchild", 122);
        Node<String, Integer> firstInitialChild = node("first-child", 11, iterableWith(firstInitialGrandchild, secondInitialGrandchild));
        Node<String, Integer> secondInitialChild = node("second-child", 12, iterableWith(thirdInitialGrandchild, fourthInitialGrandchild));
        Node<String, Integer> rootInitialNode = node("root", 1, iterableWith(firstInitialChild, secondInitialChild));

        Tree<String, Integer> initial = new Tree<>(rootInitialNode);

        Node<String, Integer> firstExpectedGrandchild = leafNode("mapped-first-grandchild", 111);
        Node<String, Integer> secondExpectedGrandchild = leafNode("mapped-second-grandchild", 112);
        Node<String, Integer> thirdExpectedGrandchild = leafNode("mapped-third-grandchild", 121);
        Node<String, Integer> fourthExpectedGrandchild = leafNode("mapped-fourth-grandchild", 122);
        Node<String, Integer> firstExpectedChild = node("mapped-first-child", 11, iterableWith(firstExpectedGrandchild, secondExpectedGrandchild));
        Node<String, Integer> secondExpectedChild = node("mapped-second-child", 12, iterableWith(thirdExpectedGrandchild, fourthExpectedGrandchild));
        Node<String, Integer> rootExpectedNode = node("mapped-root", 1, iterableWith(firstExpectedChild, secondExpectedChild));

        Tree<String, Integer> expected = new Tree<>(rootExpectedNode);

        // When
        Tree<String, Integer> actual = initial.mapLabels(new UnaryFunction<String, String>() {
            @Override public String call(String label) {
                return "mapped-" + label;
            }
        });

        // Then
        assertThat(actual, is(expected));
    }
    
    @Test
    public void zipsTwoTreesTogetherByLabelWhenIdenticalInStructure() {
        // Given
        Node<String, Integer> firstNode111 = leafNode("111", 111);
        Node<String, Integer> firstNode112 = leafNode("112", 112);
        Node<String, Integer> firstNode113 = leafNode("113", 113);
        Node<String, Integer> firstNode121 = leafNode("121", 121);
        Node<String, Integer> firstNode122 = leafNode("122", 122);
        Node<String, Integer> firstNode11 = node("11", 11, iterableWith(firstNode111, firstNode112, firstNode113));
        Node<String, Integer> firstNode12 = node("12", 12, iterableWith(firstNode121, firstNode122));
        Node<String, Integer> firstNode1 = node("1", 1, iterableWith(firstNode11, firstNode12));

        Node<String, String> secondNode111 = leafNode("111", "111");
        Node<String, String> secondNode112 = leafNode("112", "112");
        Node<String, String> secondNode113 = leafNode("113", "113");
        Node<String, String> secondNode121 = leafNode("121", "121");
        Node<String, String> secondNode122 = leafNode("122", "122");
        Node<String, String> secondNode11 = node("11", "11", iterableWith(secondNode111, secondNode112, secondNode113));
        Node<String, String> secondNode12 = node("12", "12", iterableWith(secondNode121, secondNode122));
        Node<String, String> secondNode1 = node("1", "1", iterableWith(secondNode11, secondNode12));

        Tree<String, Integer> firstTree = new Tree<>(firstNode1);
        Tree<String, String> secondTree = new Tree<>(secondNode1);

        Node<String, Pair<Integer, String>> expectedNode111 = leafNode("111", tuple(111, "111"));
        Node<String, Pair<Integer, String>> expectedNode112 = leafNode("112", tuple(112, "112"));
        Node<String, Pair<Integer, String>> expectedNode113 = leafNode("113", tuple(113, "113"));
        Node<String, Pair<Integer, String>> expectedNode121 = leafNode("121", tuple(121, "121"));
        Node<String, Pair<Integer, String>> expectedNode122 = leafNode("122", tuple(122, "122"));
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
        Node<String, Integer> firstNode111 = leafNode("111", 111);
        Node<String, Integer> firstNode112 = leafNode("112", 112);
        Node<String, Integer> firstNode113 = leafNode("113", 113);
        Node<String, Integer> firstNode121 = leafNode("121", 121);
        Node<String, Integer> firstNode122 = leafNode("122", 122);
        Node<String, Integer> firstNode11 = node("11", 11, iterableWith(firstNode111, firstNode112, firstNode113));
        Node<String, Integer> firstNode12 = node("12", 12, iterableWith(firstNode121, firstNode122));
        Node<String, Integer> firstNode1 = node("1", 1, iterableWith(firstNode11, firstNode12));

        Node<String, String> secondNode111 = leafNode("111", "111");
        Node<String, String> secondNode113 = leafNode("113", "113");
        Node<String, String> secondNode11 = node("11", "11", iterableWith(secondNode111, secondNode113));
        Node<String, String> secondNode1 = node("1", "1", iterableWith(secondNode11));

        Tree<String, Integer> firstTree = new Tree<>(firstNode1);
        Tree<String, String> secondTree = new Tree<>(secondNode1);

        Node<String, Pair<Integer, String>> expectedNode111 = leafNode("111", tuple(111, "111"));
        Node<String, Pair<Integer, String>> expectedNode113 = leafNode("113", tuple(113, "113"));
        Node<String, Pair<Integer, String>> expectedNode11 = node("11", tuple(11, "11"), iterableWith(expectedNode111, expectedNode113));
        Node<String, Pair<Integer, String>> expectedNode1 = node("1", tuple(1, "1"), iterableWith(expectedNode11));

        Tree<String, Pair<Integer, String>> expectedTree = new Tree<>(expectedNode1);

        // When
        Tree<String, Pair<Integer, String>> actualTree = firstTree.zip(secondTree);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void zipsTwoTreesTogetherByLabelIgnoringNodesWithoutACorrespondingNodeWhenZipModeIsStrict() {
        // Given
        Node<String, Integer> firstNode111 = leafNode("111", 111);
        Node<String, Integer> firstNode112 = leafNode("112", 112);
        Node<String, Integer> firstNode113 = leafNode("113", 113);
        Node<String, Integer> firstNode121 = leafNode("121", 121);
        Node<String, Integer> firstNode122 = leafNode("122", 122);
        Node<String, Integer> firstNode11 = node("11", 11, iterableWith(firstNode111, firstNode112, firstNode113));
        Node<String, Integer> firstNode12 = node("12", 12, iterableWith(firstNode121, firstNode122));
        Node<String, Integer> firstNode1 = node("1", 1, iterableWith(firstNode11, firstNode12));

        Node<String, String> secondNode111 = leafNode("111", "111");
        Node<String, String> secondNode113 = leafNode("113", "113");
        Node<String, String> secondNode11 = node("11", "11", iterableWith(secondNode111, secondNode113));
        Node<String, String> secondNode1 = node("1", "1", iterableWith(secondNode11));

        Tree<String, Integer> firstTree = new Tree<>(firstNode1);
        Tree<String, String> secondTree = new Tree<>(secondNode1);

        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode111 = leafNode("111", tuple(option(111), option("111")));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode113 = leafNode("113", tuple(option(113), option("113")));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode11 = node("11", tuple(option(11), option("11")), iterableWith(expectedNode111, expectedNode113));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode1 = node("1", tuple(option(1), option("1")), iterableWith(expectedNode11));

        Tree<String, Pair<Option<Integer>, Option<String>>> expectedTree = new Tree<>(expectedNode1);

        // When
        Tree<String, Pair<Option<Integer>, Option<String>>> actualTree = firstTree.zip(Strict, secondTree);

        // Then
        assertThat(actualTree, is(expectedTree));
    }

    @Test
    public void zipsTwoTreesTogetherByLabelKeepingNodesWithoutCorrespondingNodesFromBothTreesWhenZipModeIsLoose() {
        // Given
        Node<String, Integer> firstNode111 = leafNode("111", 111);
        Node<String, Integer> firstNode112 = leafNode("112", 112);
        Node<String, Integer> firstNode113 = leafNode("113", 113);
        Node<String, Integer> firstNode121 = leafNode("121", 121);
        Node<String, Integer> firstNode122 = leafNode("122", 122);
        Node<String, Integer> firstNode11 = node("11", 11, iterableWith(firstNode111, firstNode112, firstNode113));
        Node<String, Integer> firstNode12 = node("12", 12, iterableWith(firstNode121, firstNode122));
        Node<String, Integer> firstNode1 = node("1", 1, iterableWith(firstNode11, firstNode12));

        Node<String, String> secondNode111 = leafNode("111", "111");
        Node<String, String> secondNode113 = leafNode("113", "113");
        Node<String, String> secondNode114 = leafNode("114", "114");
        Node<String, String> secondNode11 = node("11", "11", iterableWith(secondNode111, secondNode113, secondNode114));
        Node<String, String> secondNode13 = leafNode("13", "13");
        Node<String, String> secondNode1 = node("1", "1", iterableWith(secondNode11, secondNode13));

        Tree<String, Integer> firstTree = new Tree<>(firstNode1);
        Tree<String, String> secondTree = new Tree<>(secondNode1);

        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode111 = leafNode("111", tuple(option(111), option("111")));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode112 = leafNode("112", tuple(option(112), none(String.class)));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode113 = leafNode("113", tuple(option(113), option("113")));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode114 = leafNode("114", tuple(none(Integer.class), option("114")));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode121 = leafNode("121", tuple(option(121), none(String.class)));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode122 = leafNode("122", tuple(option(122), none(String.class)));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode11 = node("11", tuple(option(11), option("11")),
                iterableWith(expectedNode112, expectedNode111, expectedNode113, expectedNode114));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode12 = node("12", tuple(option(12), none(String.class)),
                iterableWith(expectedNode121, expectedNode122));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode13 = leafNode("13", tuple(none(Integer.class), option("13")));
        Node<String, Pair<Option<Integer>, Option<String>>> expectedNode1 = node("1", tuple(option(1), option("1")),
                iterableWith(expectedNode12, expectedNode11, expectedNode13));

        Tree<String, Pair<Option<Integer>, Option<String>>> expectedTree = new Tree<>(expectedNode1);

        // When
        Tree<String, Pair<Option<Integer>, Option<String>>> actualTree = firstTree.zip(Loose, secondTree);

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