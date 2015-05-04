package org.javafunk.referee;

import org.javafunk.referee.problems.MissingFieldProblem;
import org.javafunk.referee.problems.MissingInnerBuilderProblem;
import org.javafunk.referee.problems.MissingWitherProblem;
import org.javafunk.referee.problems.NoValidMechanismProblem;

public class Problems {
    public static Problem missingInnerBuilderProblem(String attributeAddress, Class<?> type) {
        return new MissingInnerBuilderProblem(attributeAddress, type);
    }

    public static Problem missingWitherProblem(String attributeAddress, Class<?> builderType) {
        return new MissingWitherProblem(attributeAddress, builderType);
    }

    public static Problem noValidMechanism(String attributeAddress, Class<?> type) {
        return new NoValidMechanismProblem(attributeAddress, type);
    }

    public static Problem missingFieldProblem(String attributeAddress, Class<?> type) {
        return new MissingFieldProblem(attributeAddress, type);
    }
}
