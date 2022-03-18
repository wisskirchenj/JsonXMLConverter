package converter.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * extra indentation is needed only in Json-Format for Data-elements with XML-attributes,
     * because in this case, the element has an additional value-hierarchy "#tag: {...}"
     * @return 1 if data structure has attributes, 0 (the default) else.
     */
    @Override
    protected int getExtraIndent(DataStructureElement data) {
        return data.getAttributeElements() == null ? 0 : 1;
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
        generateAttributes(data, indentationLevel, false);
    }

    private void generateAttributes(DataStructureElement data, int indentationLevel, boolean isArrayElement) {
        builder.append(indent.repeat(indentationLevel));
        if (!isArrayElement) {
            builder.append(String.format("\"%s\": ", data.getAttribute()));
        }
        if (data.getAttributeElements() == null) {
            return;
        }
        builder.append("{\n");
        for (LeafElement element : data.getAttributeElements()) {
            builder.append(indent.repeat(indentationLevel + 1))
                    .append(String.format("\"%s%s\": ", "@", element.getAttribute()));
            builder.append(getLeafValueString(element.getValue()));
        }
    }

    @Override
    protected void generateEndOfParentAttribute(ParentElement data, int indentationLevel) {
        char endChar = matchesJsonArray(data.getValue()) ? '[' : '{';
        if (data.getAttributeElements() != null) {
            builder.append(indent.repeat(indentationLevel + 1))
                    .append(String.format("\"%s%s\": %c%n", "#", data.getAttribute(), endChar));
        } else {
            builder.append(endChar).append("\n");
        }
    }

    @Override
    protected void generateEndOfParent(ParentElement data, int indentationLevel) {
        builder.deleteCharAt(builder.lastIndexOf(",")); // remove last ','
        char endChar = matchesJsonArray(data.getValue()) ? ']' : '}';
        if (data.getAttributeElements() != null) {
            builder.append(indent.repeat(indentationLevel + 1)).append(endChar).append("\n");
            builder.append(indent.repeat(indentationLevel)).append("},\n");
        } else {
            builder.append(indent.repeat(indentationLevel)).append(endChar).append(",\n");
        }
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
            builder.append(indent.repeat(indentationLevel + 1))
                    .append(String.format("\"%s%s\": ", "#", data.getAttribute()));
            builder.append(getLeafValueString(data.getValue()));
            builder.deleteCharAt(builder.lastIndexOf(",")); // remove last ','
            builder.append(indent.repeat(indentationLevel)).append("},\n");
        }
    }

    /**
     * hook-method for Json-(or other) Generator to implement special array generation logic.
     * The method must return false (as in this default implementation), if normal list generation
     * should be performed instead.
     * @param parent parent element whose value list could be an array
     * @return true if array generation has been performed - nothing else is generated in this case
     *          or false if normal generation should be performed
     */
    @Override
    protected boolean generateArray(ParentElement parent, int indentationLevel) {
        if (!matchesJsonArray(parent.getValue())) {
            return false;
        }
        for (DataStructureElement entry : parent.getValue()) {
            generateAttributes(entry, indentationLevel + 1 + getExtraIndent(parent), true);
            if (entry instanceof LeafElement leafElement) {
                generateLeafValue(leafElement, indentationLevel + 1 + getExtraIndent(parent));
            } else {
                builder.append("{\n");
                //recursive call over array elements
                ((ParentElement) entry).getValue().forEach(data -> recursiveGenerate(data,
                        indentationLevel + 2 + getExtraIndent(parent)));
                builder.deleteCharAt(builder.lastIndexOf(",")); // remove last ','
                builder.append(indent.repeat(indentationLevel + 1 + getExtraIndent(parent))).append("},\n");
            }
        }
        return true;
    }

    /**
     * a child element list with more than one entry and all tags have the same attribute name is
     * rendered as a Json array with [ ] braces. This method checks that.
     * @param values a child element list
     * @return true, if this child element list is a Json array, false otherwise
     */
    private boolean matchesJsonArray(List<DataStructureElement> values) {
        Set<String> attributesSet = new HashSet<>();
        values.forEach(valueEntry -> attributesSet.add(valueEntry.getAttribute()));
        return values.size() > 1 && attributesSet.size() == 1;
    }

}
