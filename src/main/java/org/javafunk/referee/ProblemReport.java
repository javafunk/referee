package org.javafunk.referee;

import lombok.Value;

@Value
public class ProblemReport {
    Boolean hasProblem;

    public static ProblemReport empty() {
        return new ProblemReport(false);
    }

    public boolean hasProblems() {
        return hasProblem;
    }

    public boolean hasNoProblems() {
        return !hasProblems();
    }
}
