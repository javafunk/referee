package org.javafunk.referee;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class Parser {
    @SuppressWarnings("unchecked")
    public static Map<Object, Object> parse(String yamlString) {
        return (Map<Object, Object>) new Yaml().load(yamlString);
    }
}
