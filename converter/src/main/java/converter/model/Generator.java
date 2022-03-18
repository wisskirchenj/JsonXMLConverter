package converter.model;

/**
 * abstract generator, that bundles the recursion functionality of the
 * XML and JSon-Generator - and a third GenericGenerator for project stage 3.
 */
public abstract class Generator {

    protected String indent = "  ";
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

        if (data instanceof LeafElement leafElement) {
            generateLeafValue(leafElement, indentationLevel);
            return;
        }

        ParentElement parent = (ParentElement) data;
        generateEndOfParentAttribute(parent, indentationLevel);

        // generateArray-method to implement Json-abbreviated array format: "[<element-list>]"
        if (!generateArray(parent, indentationLevel)) {
            for (DataStructureElement value : parent.getValue()) {
                recursiveGenerate(value, indentationLevel + 1 + getExtraIndent(parent));
            }
        }

        generateEndOfParent(parent, indentationLevel);
    }

    /**
     * gives subclass generators the opportunity to apply some own indentation logic or
     * changes for special cases (both is used in derivations...)
     * @return 1 if data structure has attributes, 0 (the default) else.
     */
    protected int getExtraIndent(DataStructureElement data) {
        //default implementation
        return 0;
    }

    /**
     * Generator-subclasses can start deeper indented (needed for Json)
     * @return initial indentation level
     */
    protected int getInitialIndentLevel() {
        //default implementation
        return 0;
    }

    /**
     * Generator-subclasses can specify some starter string (needed for Json: "{\n" )
     * @return the string to append to the StringBuilder
     */
    protected String getInitialText() {
        //default implementation
        return "";
    }

    /**
     * Generator-subclasses can specify some ending string (needed for Json: "}\n" )
     * @return the string to append to the StringBuilder
     */
    protected  String getFinalText() {
        //default implementation
        return "";
    }

    /**
     * append an end marker after generating the element attribute (e.g. ">\n" in XML)
     * @param data associated DataStructureElement
     * @param indentationLevel recursion depth for indentation (e.g. used in Json)
     */
    protected void generateEndOfParentAttribute(ParentElement data, int indentationLevel) {
        // default does nothing - as in GenericGenerator
    }

    /**
     * get String for a key:value line of an attribute in the appropriate format
     * method is not abstract, as the implementation is used as a default.
     * @param attribute attribute element's attribute name
     * @param value attribute element's value in quotes
     */
    protected String getAttributesElementString(String attribute, String value) {
        //default implementation as used in GenericGenerator
        return String.format(" %s = \"%s\"", attribute, value);
    }

    /**
     * hook-method for Json-(or other) Generator to implement special array generation logic.
     * The method must return false (as in this default implementation), if normal list generation
     * should be performed instead.
     * @param parent parent element
     * @return true if array generation has been performed - nothing else is generated in this case
     *          or false if normal generation should be performed
     */
    protected boolean generateArray(ParentElement parent, int indentationLevel) {
        //default implementation - only Json-Generator uses an array abbreviation format (so far)
        return false;
    }

    /**
     * append an end marker after generating a parent structure object (e.g. "},\n" in XML)
     * @param data associated DataStructureElement
     * @param indentationLevel recursion depth for indentation (e.g. used in Json)
     */
    protected abstract void generateEndOfParent(ParentElement data, int indentationLevel);

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
