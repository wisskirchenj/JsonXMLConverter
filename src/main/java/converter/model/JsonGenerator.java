package converter.model;

/**
 * converts an abstract data structure attribute:value into text-lines in Json-format.
 */
public class JsonGenerator extends Generator {
    private static final String START_LINE = "{\n";
    private static final String END_LINE = "\n}\n";


    @Override
    protected String getFinalText() {
        return END_LINE;
    }

    @Override
    protected String getInitialText() {
        return START_LINE;
    }

    /**
     * project specification declares XML tags with attributes to be generated in a special form:
     * the value of such an XML-native element is a Json object, that
     * consists of attribute:value pairs, where the Json-attribute is marked with an @sign ("@attrib" = "val")
     * possible tag content is generated in Json as element #tagname:content.
     * Ex: <xml a1="v1" a2="v2">data</xml> gets:    "xml": { "@a1": "v1", "@a2":"v2", "#xml":"data }
     *    or <xml attr="val"/>             gets     "xml" : { "@attr":"val", "#xml":null }
     * @param data the generic key - attributes - value element to be generated
     * @param indentationLevel the indentation (recursion) level, how deep the element has to be indented
     */
    @Override
    protected void generateLeafAttributesElement(LeafAttributesElement data, int indentationLevel) {
        builder.append("{\n");
        for (LeafElement element : data.getAttributeElements()) {
            builder.append(INDENT.repeat(indentationLevel + 1));
            builder.append(getAttributeString("@" + element.getAttribute()));
            builder.append(getLeafValueString("", element.getValue()));
        }
        builder.append(INDENT.repeat(indentationLevel + 1));
        builder.append(getAttributeString("#" + data.getAttribute()));
        builder.append(getLeafValueString("", data.getValue()));
        builder.deleteCharAt(builder.lastIndexOf(",")); // remove last ','
        builder.append(INDENT.repeat(indentationLevel)).append("}");
    }

    @Override
    protected String getAttributeString(String attribute) {
        return String.format("\"%s\": ", attribute);
    }

    @Override
    protected int getInitialIndentLevel() {
        return 1;
    }

    @Override
    protected String getLeafValueString(String attribute, String value) {
        return value.equals("") ? "null,\n" : String.format("\"%s\",%n",
                value.replaceAll("\"", "\\\\\""));
    }
}
