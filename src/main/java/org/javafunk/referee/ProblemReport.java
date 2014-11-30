package org.javafunk.referee;

import com.google.common.collect.Iterables;
import lombok.Value;

import static org.javafunk.funk.Literals.iterableOf;
import static org.javafunk.funk.Literals.iterableWith;

@Value
public class ProblemReport {
    Iterable<Problem> problems;

    public static ProblemReport with(Problem problem) {
        return new ProblemReport(iterableWith(problem));
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
}
