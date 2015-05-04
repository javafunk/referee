package org.javafunk.referee.problems;

import lombok.Value;
import org.javafunk.referee.Problem;

import java.io.Writer;

@Value
public class MissingFieldProblem implements Problem {
    String attributeAddress;
    Class<?> type;

    @Override public void writeTo(Writer writer) {

    }
}
