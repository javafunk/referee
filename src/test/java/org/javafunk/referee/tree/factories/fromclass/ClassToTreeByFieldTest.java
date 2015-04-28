package org.javafunk.referee.tree.factories.fromclass;

import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedField;
import org.javafunk.referee.testclasses.ThingWithStrings;
import org.javafunk.referee.testclasses.ThingWithThingsWithStrings;
import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.Tree;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.referee.tree.Node.branchNode;
import static org.javafunk.referee.tree.Node.node;
import static org.javafunk.referee.tree.Tree.tree;
import static org.javafunk.referee.tree.factories.fromclass.ClassToTreeByField.fromClassToTreeByField;

public class ClassToTreeByFieldTest {
    @Test
    public void constructsTreeOfDepthOneFromSimpleClass() {
        // Given
        Class<?> sourceClass = ThingWithStrings.class;

        EnrichedClass<ThingWithStrings> enrichedSourceClass = new EnrichedClass<>(ThingWithStrings.class);
        EnrichedField enrichedFieldOne = enrichedSourceClass.findFieldWithName("one").get();
        EnrichedField enrichedFieldTwo = enrichedSourceClass.findFieldWithName("two").get();
        EnrichedField enrichedFieldThree = enrichedSourceClass.findFieldWithName("three").get();

        Tree<String, Object> expected = tree(branchNode("$", iterableWith(
                Node.<String, Object>leafNode("one", enrichedFieldOne),
                Node.<String, Object>leafNode("two", enrichedFieldTwo),
                Node.<String, Object>leafNode("three", enrichedFieldThree))));

        // When
        Tree<String, Object> actual = fromClassToTreeByField().call(sourceClass);

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void constructsTreeOfDepthTwoFromComplexClass() {
        // Given
        Class<?> sourceClass = ThingWithThingsWithStrings.class;
    }
}