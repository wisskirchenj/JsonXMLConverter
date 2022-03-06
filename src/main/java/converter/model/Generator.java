package converter.model;

import java.util.List;

/**
 * abstract generator, that bundles the recursion functionality of the
 * XML and JSon-Generator - and a third GenericGenerator for project stage 3.
 */
public abstract class Generator {

    protected String INDENT = "  ";
    protected StringBuilder builder;

    /**
     * entry point to start generation of Output-format
     * @param data a (probably nested) generic data object that represents the user inputs'
     *             data structure and is produced by a XMLParser or JsonParser object.
     * @return generated output
     */
    public String generate(DataStructureElement data) {
        builder = new StringBuilder(getInitialText());
        recursiveGenerate(data, getInitialIndentLevel());
        builder.append(getFinalText());
        return builder.toString();
    }

    /**
     * the central recursive generator method, called by generate(data)
     * @param data some DataStructureElement, which should be generated in the format desired
     * @param indentationLevel recursion depth, mainly used for indentation
     */
    protected void recursiveGenerate(DataStructureElement data, int indentationLevel) {
        generateAttributes(data, indentationLevel);

        if (data instanceof LeafElement) {
            generateLeafValue((LeafElement) data, indentationLevel);
            return;
        }

        generateEndOfParentAttribute(data, indentationLevel);
        List<DataStructureElement> values = ((ParentElement) data).getValue();
        for (DataStructureElement value : values) {
            recursiveGenerate(value, data.getAttributeElements() == null ? indentationLevel + 1
                    : indentationLevel + 2);
        }
        generateEndOfParent(data, indentationLevel);
    }

    /**
     * Generator-subclasses can start deeper indented (needed for Json)
     * @return initial indentation level
     */
    protected abstract int getInitialIndentLevel();

    /**
     * Generator-subclasses can specify some starter string (needed for Json: "{\n" )
     * @return the string to append to the StringBuilder
     */
    protected abstract String getInitialText();

    /**
     * Generator-subclasses can specify some ending string (needed for Json: "}\n" )
     * @return the string to append to the StringBuilder
     */
    protected abstract String getFinalText();

    /**
     * append an end marker after generating the element attribute (e.g. ">\n" in XML)
     * @param data associated DataStructureElement
     * @param indentationLevel recursion depth for indentation (e.g. used in Json)
     */
    protected abstract void generateEndOfParentAttribute(DataStructureElement data, int indentationLevel);

    /**
     * get String for a key:value line of an attribute in the appropriate format
     * method is not abstract, as the implementation is used as a default.
     * @param attribute attribute element's attribute name
     * @param value attribute element's value in quotes
     */
    protected String getAttributesElementString(String attribute, String value) {
        return String.format(" %s = \"%s\"", attribute, value);
    }

    /**
     * append an end marker after generating a parent structure object (e.g. "},\n" in XML)
     * @param data associated DataStructureElement
     * @param indentationLevel recursion depth for indentation (e.g. used in Json)
     */
    protected abstract void generateEndOfParent(DataStructureElement data, int indentationLevel);

    /** only for LeafElements, the value string is appended by this method hook in the format
     * @param data associated LeafElement
     * @param indentationLevel recursion depth for indentation (e.g. used in Json)
     */
    protected abstract void generateLeafValue(LeafElement data, int indentationLevel);

    /** generation of XML-element with attributes and its project-specific counterpart-implementation
     * in Json is very different for those types - so this is pure format specific logic.
     * @param data associated DataStructureElement
     * @param indentationLevel recursion depth for indentation (e.g. used in Json)
     */
    protected abstract void generateAttributes(DataStructureElement data, int indentationLevel);
}
