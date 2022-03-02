package converter.model;

/**
 * abstract generator, that bundles the recursion functionality of the
 * XML and JSon-Generator - and a third-generict type to be implemented in project stage 3.
 */
public abstract class Generator {

    protected static final String INDENT = "  ";
    protected StringBuilder builder;

    /**
     * entry point to start generation of Output-format
     * @return geenrated output
     */
    public String generate(DataStructureElement data) {
        builder = new StringBuilder(getInitialText());
        recursiveGenerate(data, getInitialIndentLevel());
        builder.append(getFinalText());
        return builder.toString();
    }

    protected abstract int getInitialIndentLevel();

    protected abstract String getInitialText();

    protected abstract String getFinalText();

    protected void recursiveGenerate(DataStructureElement data, int indentationLevel) {
        builder.append(INDENT.repeat(indentationLevel)).append(getAttributeString(data.getAttribute()));
        if (data instanceof LeafElement) {
            builder.append(getLeafValueString(data.getAttribute(), (String) data.getValue()));
            return;
        }

        if (data instanceof LeafAttributesElement) {
            generateLeafAttributesElement((LeafAttributesElement) data, indentationLevel);
            return;
        }

        throw new JsonXMLParseException("Json-XML Generator: ParentElements not supported yet!");

        /*List<DataStructureElement> values = (List<DataStructureElement>) data.getValue();
         for (DataStructureElement value : values) recursiveGenerate(value, i+1);
         write end-attribute tag
         return */
    }

    /** generation of XML-element with attributes and its project-specfic counterpart-implementation
     * in Json is very different for those types - so this is pure format specific logic.
     * @param data the generic key - attributes - value element to be generated
     * @param indentationLevel the indentation (recursion) level, how deep the element has to be indented
     */
    protected abstract void generateLeafAttributesElement(LeafAttributesElement data, int indentationLevel);

    protected abstract String getLeafValueString(String attribute, String value);

    protected abstract String getAttributeString(String attribute);
}
