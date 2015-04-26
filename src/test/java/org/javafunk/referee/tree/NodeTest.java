package org.javafunk.referee.tree;

import org.javafunk.funk.matchers.OptionMatchers;
import org.javafunk.funk.monads.Option;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.funk.matchers.OptionMatchers.hasValue;
import static org.javafunk.referee.tree.Node.leafNode;
import static org.javafunk.referee.tree.Node.node;

public class NodeTest {
    @Test
    public void returnsNodeAsOptionWhenFindingChildByLabelAndChildPresent() {
        // Given
        Node<String, String> node11 = leafNode("11", "one-one");
        Node<String, String> node12 = leafNode("12", "one-two");
        Node<String, String> node13 = leafNode("13", "one-three");
        Node<String, String> node1 = node("1", "one", iterableWith(node11, node12, node13));

        String label = "12";
        Node<String, String> expected = node12;

        // When
        Option<Node<String, String>> actual = node1.findChildBy(label);

        // Then
        assertThat(actual, hasValue(expected));
    }

    @Test
    public void returnsNoneWhenFindingChildByLabelAndChildAbsent() {
        // Given
        Node<String, String> node11 = leafNode("11", "one-one");
        Node<String, String> node12 = leafNode("12", "one-two");
        Node<String, String> node13 = leafNode("13", "one-three");
        Node<String, String> node1 = node("1", "one", iterableWith(node11, node12, node13));

        String label = "14";

        // When
        Option<Node<String, String>> actual = node1.findChildBy(label);

        // Then
        assertThat(actual, OptionMatchers.<Node<String, String>>hasNoValue());
    }

    @Test
    public void returnsNodeAsOptionWhenFindingDescendantByLabelAndDescendantIsPresent() {
        // Given
        Node<String, String> node112 = leafNode("112", "one-one-two");
        Node<String, String> node111 = leafNode("111", "one-one-one");
        Node<String, String> node11 = node("11", "one-one", iterableWith(node111, node112));
        Node<String, String> node12 = leafNode("12", "one-two");
        Node<String, String> node13 = leafNode("13", "one-three");
        Node<String, String> node1 = node("1", "one", iterableWith(node11, node12, node13));

        String label = "112";
        Node<String, String> expected = node112;

        // When
        Option<Node<String, String>> actual = node1.findDescendantBy(label);

        // Then
        assertThat(actual, hasValue(expected));
    }

    @Test
    public void returnsNoneWhenFindingDescendantByLabelAndDescendantIsNotPresent() {
        // Given
        Node<String, String> node112 = leafNode("112", "one-one-two");
        Node<String, String> node111 = leafNode("111", "one-one-one");
        Node<String, String> node11 = node("11", "one-one", iterableWith(node111, node112));
        Node<String, String> node12 = leafNode("12", "one-two");
        Node<String, String> node13 = leafNode("13", "one-three");
        Node<String, String> node1 = node("1", "one", iterableWith(node11, node12, node13));

        String label = "113";

        // When
        Option<Node<String, String>> actual = node1.findDescendantBy(label);

        // Then
        assertThat(actual, OptionMatchers.<Node<String, String>>hasNoValue());
    }

    @Test
    public void returnsTrueForHasLabelWhenNodeHasSuppliedLabel() {
        // Given
        Node<String, String> node = leafNode("1", "one");

        // When
        boolean hasLabel = node.hasLabel("1");

        // Then
        assertThat(hasLabel, is(true));
    }

    @Test
    public void returnsFalseForHasLabelWhenNodeDoesNotHaveSuppliedLabel() {
        // Given
        Node<String, String> node = leafNode("1", "one");

        // When
        boolean hasLabel = node.hasLabel("2");

        // Then
        assertThat(hasLabel, is(false));
    }
}