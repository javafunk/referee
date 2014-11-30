package org.javafunk.referee;

import lombok.SneakyThrows;
import lombok.Value;

import java.io.Writer;

@Value
public class NoValidMechanismProblem implements Problem {
    String address;
    Class<?> type;

    @SneakyThrows
    @Override public void writeTo(Writer writer) {
        writer.append("No value mechanism found for class: ")
                .append(type.getSimpleName())
                .append(" at address: ")
                .append(address);
    }
}
