package org.javafunk.referee.attributename;

public class TextualAttributeNameResolver implements AttributeNameResolver {
    @Override
    public String resolve(Object attributeNameObject) {
        String attributeNameString = attributeNameObject.toString();
        String joined = attributeNameString.replace(" ", "");
        String firstLetter = attributeNameString.substring(0, 1);
        String attributeName = firstLetter.toLowerCase() + joined.substring(1);

        return attributeName;
    }
}
