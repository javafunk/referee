package org.javafunk.referee;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.testclasses.ThingWithBuilderAndStrings;
import org.javafunk.referee.testclasses.ThingWithNoBuilder;
import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.Tree;
import org.javafunk.referee.tree.Visitor;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.referee.Parser.parse;
import static org.javafunk.referee.PopulationEngineBuilder.populationEngine;
import static org.javafunk.referee.Problems.missingInnerBuilderProblem;
import static org.javafunk.referee.tree.Node.branchNode;
import static org.javafunk.referee.tree.Node.node;
import static org.javafunk.referee.tree.Tree.tree;

public class TreeBasedIntegrationTest {
    @Test
    public void populatesObjectWithOnlyStringFields() throws Exception {
        // Given
        Map<Object, Object> definition = parse(
                "One: The first string\n" +
                "Two: The second string\n" +
                "Three: The third string");

        // When
        PopulationResult<ThingWithBuilderAndStrings> result = populationEngine()
                .usingTreeBasedApproach()
                .forType(ThingWithBuilderAndStrings.class)
                .process(definition);

        // Then
        assertThat(result.getInstance(), is(new ThingWithBuilderAndStrings(
                "The first string",
                "The second string",
                "The third string")));
    }

    @Test(enabled = false)
    public void reportsProblemWhenNoInnerBuilderFoundForTargetType() throws Exception {
        // Given
        Node<String, Object> firstChild = Node.<String, Object>leafNode("One", "First");
        Node<String, Object> secondChild = Node.<String, Object>leafNode("Two", "Second");
        Node<String, Object> rootNode = branchNode("$", iterableWith(firstChild, secondChild));

        Tree<String, Object> tree = tree(rootNode);

        // When
        ProblemFindingVisitor visitor = tree.visit(ProblemFindingVisitor.findingProblemsFor(ThingWithNoBuilder.class));

        // Then
        assertThat(visitor.getProblemReport(),
                is(ProblemReport.of(
                        missingInnerBuilderProblem("$", ThingWithNoBuilder.class))));
    }

    @Value
    @AllArgsConstructor
    public static class ProblemFindingVisitor<C> implements Visitor<String, Object, ProblemFindingVisitor<C>> {
        Class<C> targetClass;
        ProblemReport problemReport;

        public static <C> ProblemFindingVisitor<C> findingProblemsFor(Class<C> targetClass) {
            return new ProblemFindingVisitor<>(targetClass);
        }

        public ProblemFindingVisitor(Class<C> targetClass) {
            this(targetClass, ProblemReport.empty());
        }

        @Override
        public ProblemFindingVisitor<C> visit(Node<String, Object> node) {
            EnrichedClass<C> enrichedClass = new EnrichedClass<>(targetClass);
            Option<EnrichedClass<?>> possibleBuilderClass = enrichedClass
                    .findInnerClassWithName("Builder");

            if (possibleBuilderClass.hasNoValue()) {
                return new ProblemFindingVisitor<>(targetClass,
                        problemReport.with(missingInnerBuilderProblem(node.getLabel(), targetClass)));
            }

            return this;
        }
    }
}
