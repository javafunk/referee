package org.javafunk.referee.mechanisms;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.javafunk.funk.Eagerly;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.Literals;
import org.javafunk.funk.Predicates;
import org.javafunk.funk.functors.Mapper;
import org.javafunk.referee.Problem;
import org.javafunk.referee.ProblemReport;
import org.javafunk.referee.conversion.FunctionBasedCoercionEngine;
import org.javafunk.referee.testclasses.ThingWithBuilder;
import org.javafunk.referee.testclasses.ThingWithBuilderAndMissingWither;
import org.javafunk.referee.testclasses.ThingWithBuilderAndMissingWithers;
import org.javafunk.referee.testclasses.ThingWithNoBuilder;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.*;
import static org.javafunk.funk.Predicates.equalTo;
import static org.javafunk.referee.Problems.missingInnerBuilderProblem;
import static org.javafunk.referee.Problems.missingWitherProblem;

public class BuilderPopulationMechanismFactoryTest {
    @Test
    public void hasNoProblemIfTargetTypeHasInnerBuilderClassAndWithersForAllFields() throws Exception {
        // Given
        Class<ThingWithBuilder> targetType = ThingWithBuilder.class;
        Map<Object, Object> definition = mapOf(Object.class, Object.class);

        BuilderPopulationMechanismFactory mechanismFactory = new BuilderPopulationMechanismFactory(
                FunctionBasedCoercionEngine.withDefaultCoercions());

        // When
        ProblemReport report = mechanismFactory.validateFor(targetType, definition, ProblemReport.empty());

        // Then
        assertThat(report.hasProblems(), is(false));
    }

    @Test
    public void hasProblemIfTargetTypeHasNoInnerBuilderClass() throws Exception {
        // Given
        Class<ThingWithNoBuilder> targetType = ThingWithNoBuilder.class;
        Map<Object, Object> definition = mapOf(Object.class, Object.class);

        BuilderPopulationMechanismFactory mechanismFactory = new BuilderPopulationMechanismFactory(
                FunctionBasedCoercionEngine.withDefaultCoercions());

        // When
        ProblemReport report = mechanismFactory.validateFor(targetType, definition, ProblemReport.empty());

        // Then
        assertThat(report, hasProblem(missingInnerBuilderProblem("$", ThingWithNoBuilder.class)));
    }

    @Test
    public void hasProblemIfTargetTypeIsMissingAWither() throws Exception {
        // Given
        Class<ThingWithBuilderAndMissingWither> targetType = ThingWithBuilderAndMissingWither.class;
        Map<Object, Object> definition = Literals.<Object, Object>mapWithKeyValuePair("No Wither", "Value");

        BuilderPopulationMechanismFactory mechanismFactory = new BuilderPopulationMechanismFactory(
                FunctionBasedCoercionEngine.withDefaultCoercions());

        // When
        ProblemReport report = mechanismFactory.validateFor(targetType, definition, ProblemReport.empty());

        // Then
        assertThat(report, hasProblem(missingWitherProblem("$.noWither", ThingWithBuilderAndMissingWither.Builder.class)));
    }

    @Test
    public void reportsAllMissingWithProblemsAtOnce() throws Exception {
        // Given
        Class<ThingWithBuilderAndMissingWithers> targetType = ThingWithBuilderAndMissingWithers.class;
        Map<Object, Object> definition = Literals.<Object, Object>mapWithKeyValuePairs(
                "First Missing Wither", "Value 1",
                "Second Missing Wither", "Value 2",
                "Present Wither", "Value 3");

        BuilderPopulationMechanismFactory mechanismFactory = new BuilderPopulationMechanismFactory(
                FunctionBasedCoercionEngine.withDefaultCoercions());

        // When
        ProblemReport report = mechanismFactory.validateFor(targetType, definition, ProblemReport.empty());

        // Then
        assertThat(report, hasProblems(
                missingWitherProblem("$.firstMissingWither", ThingWithBuilderAndMissingWithers.Builder.class),
                missingWitherProblem("$.firstMissingWither", ThingWithBuilderAndMissingWithers.Builder.class)));
    }

    private Matcher<ProblemReport> hasProblems(Problem... problems) {
        return Matchers.allOf(Lazily.map(iterableFrom(problems), new Mapper<Problem, Matcher<? super ProblemReport>>() {
            @Override public Matcher<ProblemReport> map(Problem problem) {
                return hasProblem(problem);
            }
        }));
    }

    private Matcher<ProblemReport> hasProblem(final Problem problem) {
        return new TypeSafeDiagnosingMatcher<ProblemReport>() {
            @Override protected boolean matchesSafely(ProblemReport problemReport, Description mismatchDescription) {
                if(Eagerly.any(problemReport.getProblems(), equalTo(problem))) {
                    return true;
                }
                mismatchDescription
                        .appendText("got problem report: ")
                        .appendValue(problemReport);
                return false;
            }

            @Override public void describeTo(Description description) {
                description.appendText("a problem report containing problem: ")
                        .appendValue(problem);
            }
        };
    }
}