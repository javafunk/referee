package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedField;
import org.javafunk.referee.testclasses.ThingWithString;
import org.javafunk.referee.testclasses.ThingWithStrings;
import org.javafunk.referee.testclasses.ThingWithThingsWithStrings;
import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.Tree;
import org.testng.annotations.Test;

import javax.lang.model.element.Element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.referee.tree.Node.branchNode;
import static org.javafunk.referee.tree.Node.leafNode;
import static org.javafunk.referee.tree.Node.node;
import static org.javafunk.referee.tree.Tree.tree;
import static org.javafunk.referee.tree.factories.fromclass.ClassToTree.fromClassToTree;

public class ClassToTreeTest {
    @Test
    public void constructsTreeOfDepthOneFromSimpleClass() {
        // Given
        Class<?> sourceClass = ThingWithStrings.class;

        EnrichedClass<ThingWithStrings> enrichedSourceClass = new EnrichedClass<>(ThingWithStrings.class);
        ElementMetadata metadataForClass = ElementMetadata.forClass(enrichedSourceClass);
        ElementMetadata metadataForFieldOne = ElementMetadata.forField(new EnrichedClass<>(String.class), enrichedSourceClass.findFieldWithName("one").get());
        ElementMetadata metadataForFieldTwo = ElementMetadata.forField(new EnrichedClass<>(String.class), enrichedSourceClass.findFieldWithName("two").get());
        ElementMetadata metadataForFieldThree = ElementMetadata.forField(new EnrichedClass<>(String.class), enrichedSourceClass.findFieldWithName("three").get());

        Tree<String, ElementMetadata> expected = tree(node("$", metadataForClass, iterableWith(
                leafNode("one", metadataForFieldOne),
                leafNode("two", metadataForFieldTwo),
                leafNode("three", metadataForFieldThree))));

        // When
        Tree<String, ElementMetadata> actual = fromClassToTree().call(sourceClass);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void constructsTreeOfDepthTwoFromComplexClass() {
        // Given
        Class<?> sourceClass = ThingWithThingsWithStrings.class;

        EnrichedClass<ThingWithThingsWithStrings> enrichedSourceClass = new EnrichedClass<>(ThingWithThingsWithStrings.class);
        EnrichedClass<ThingWithString> enrichedNestedClass = new EnrichedClass<>(ThingWithString.class);

        ElementMetadata metadataForClass = ElementMetadata.forClass(enrichedSourceClass);
        ElementMetadata metadataForFirstField = ElementMetadata.forField(enrichedNestedClass, enrichedSourceClass.findFieldWithName("first").get());
        ElementMetadata metadataForSecondField = ElementMetadata.forField(enrichedNestedClass, enrichedSourceClass.findFieldWithName("second").get());
        ElementMetadata metadataForFirstStringField = ElementMetadata.forField(new EnrichedClass<>(String.class), enrichedNestedClass.findFieldWithName("string").get());
        ElementMetadata metadataForSecondStringField = ElementMetadata.forField(new EnrichedClass<>(String.class), enrichedNestedClass.findFieldWithName("string").get());

        Tree<String, ElementMetadata> expected = tree(node("$", metadataForClass, iterableWith(
                node("first", metadataForFirstField, iterableWith(leafNode("string", metadataForFirstStringField))),
                node("second", metadataForSecondField, iterableWith(leafNode("string", metadataForSecondStringField))))));

        // When
        Tree<String, ElementMetadata> actual = fromClassToTree().call(sourceClass);

        // Then
        assertThat(actual, is(expected));
    }
}