package converter.model;

/**
 * converts an abstract data structure attribute:value into text-lines in Json-format.
 */
public class JsonGenerator extends Generator {
    private static final String START_LINE = "{\n";
    private static final String END_LINE = "}\n";

    /**
     * as a side effect, this method removes an extra ',', that is appended to each
     * parent element at the end...
     * @return the end line
     */
    @Override
    protected String getFinalText() {
        builder.deleteCharAt(builder.lastIndexOf(",")); // remove last ','
        return END_LINE;
    }

    @Override
    protected int getInitialIndentLevel() {
        return 1;
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
    protected void generateAttributes(DataStructureElement data, int indentationLevel) {
        builder.append(INDENT.repeat(indentationLevel))
                .append(String.format("\"%s\": ", data.getAttribute()));
        if (data.getAttributeElements() == null) {
            return;
        }
        builder.append("{\n");
        for (LeafElement element : data.getAttributeElements()) {
            builder.append(INDENT.repeat(indentationLevel + 1))
                    .append(String.format("\"%s%s\": ", "@", element.getAttribute()));
            builder.append(getLeafValueString(element.getValue()));
        }
        if (!(data instanceof LeafElement)) {
            builder.deleteCharAt(builder.lastIndexOf(",")); // remove last ','
            builder.append(INDENT.repeat(indentationLevel)).append("}\n");
        }
    }

    @Override
    protected void generateEndOfParentAttribute(DataStructureElement data, int indentationLevel) {
        builder.append(INDENT.repeat(indentationLevel)).append("{\n");
    }

    @Override
    protected void generateEndOfParent(DataStructureElement data, int indentationLevel) {
        builder.deleteCharAt(builder.lastIndexOf(",")); // remove last ','
        builder.append(INDENT.repeat(indentationLevel)).append("},\n");
    }

    private String getLeafValueString(String value) {
        return value == null ? "null,\n" : String.format("\"%s\",%n", value);
    }

    /**
     * method contains some logic, as it can handle XML-elements with attributes as
     * well as standard XML-elements, that better correspond to Json-objects
     * @param data associated LeafElement
     * @param indentationLevel recursion depth for indentation (e.g. used in Json)
     */
    @Override
    protected void generateLeafValue(LeafElement data, int indentationLevel) {
        if (data.getAttributeElements() == null) {
            builder.append(getLeafValueString(data.getValue()));
        } else {
            builder.append(INDENT.repeat(indentationLevel + 1))
                    .append(String.format("\"%s%s\": ", "#", data.getAttribute()));
            builder.append(getLeafValueString(data.getValue()));
            builder.deleteCharAt(builder.lastIndexOf(",")); // remove last ','
            builder.append(INDENT.repeat(indentationLevel)).append("},\n");
        }
    }
}
