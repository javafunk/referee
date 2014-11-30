package org.javafunk.referee;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class Parser {
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parse(String yamlString) {
        return (Map<String, Object>) new Yaml().load(yamlString);
    }
}
