package org.javafunk.referee.problems;

import lombok.SneakyThrows;
import lombok.Value;
import org.javafunk.referee.Problem;

import java.io.Writer;

@Value
public class MissingWitherProblem implements Problem {
    String address;
    Class<?> builderType;

    @SneakyThrows
    @Override public void writeTo(Writer writer) {
        writer.append("No wither found for address: ")
                .append(address)
                .append(" on builder type: ")
                .append(builderType.getSimpleName());
    }
}
