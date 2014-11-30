package org.javafunk.referee;

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
}
