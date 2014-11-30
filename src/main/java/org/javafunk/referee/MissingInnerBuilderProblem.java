package org.javafunk.referee;

import lombok.SneakyThrows;
import lombok.Value;

import java.io.Writer;

@Value
public class MissingInnerBuilderProblem implements Problem {
    String address;
    Class<?> type;

    @SneakyThrows
    @Override public void writeTo(Writer writer) {
        writer.append("No inner Builder found at address: ")
                .append(address)
                .append(" for class: ")
                .append(type.getSimpleName());
    }
}
