package org.javafunk.referee;

import org.javafunk.referee.testclasses.ThingWithString;
import org.javafunk.referee.testclasses.ThingWithStrings;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.javafunk.matchbox.Matchers.hasOnlyItemsInAnyOrder;
import static org.javafunk.referee.Parser.parse;
import static org.javafunk.referee.Problems.missingFieldProblem;

public class ProblemReportingIntegrationTest {
    @Test
    public void reportsWhenFieldInInputIsNotInOutput() {
        // Given
        Map<Object, Object> thing = parse(
                "iterable:\n" +
                " - 1\n" +
                " - 2\n");

        // When
        ProblemFinder problemFinder = new ProblemFinder(thing, ThingWithString.class);

        // Then
        assertThat(problemFinder.getReport().getProblems(),
                hasOnlyItemsInAnyOrder(missingFieldProblem("$.iterable", ThingWithString.class)));
    }

    @Test
    public void reportsWhenMultipleFieldsInInputAreNotInOutput() {
        // Given
        Map<Object, Object> thing = parse(
                "one: 1\n" +
                "two: 2\n" +
                "string: hi\n");

        // When
        ProblemFinder problemFinder = new ProblemFinder(thing, ThingWithString.class);

        // Then
        assertThat(problemFinder.getReport().getProblems(),
                hasOnlyItemsInAnyOrder(
                        missingFieldProblem("$.one", ThingWithString.class),
                        missingFieldProblem("$.two", ThingWithString.class)));
    }
}
