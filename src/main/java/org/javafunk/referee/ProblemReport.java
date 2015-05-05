package org.javafunk.referee;

import com.google.common.collect.Iterables;
import lombok.Value;

import static org.javafunk.funk.Literals.iterableBuilderFrom;
import static org.javafunk.funk.Literals.iterableOf;

@Value
public class ProblemReport {
    Iterable<Problem> problems;

    public static ProblemReport of(Problem problem) {
        return ProblemReport.empty().with(problem);
    }

    public static ProblemReport of(Iterable<Problem> problems) {
        return new ProblemReport(problems);
    }

    public static ProblemReport empty() {
        return new ProblemReport(iterableOf(Problem.class));
    }

    public boolean hasProblems() {
        return !Iterables.isEmpty(problems);
    }

    public boolean hasNoProblems() {
        return !hasProblems();
    }

    public ProblemReport with(Problem problem) {
        return new ProblemReport(iterableBuilderFrom(problems).with(problem).build());
    }
}
