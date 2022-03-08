package converter.model;

/**
 * converts an abstract data structure attribute:value into text-lines in XML-format.
 */
public class XMLGenerator extends Generator {

    @Override
    protected void generateLeafValue(LeafElement data, int indentationLevel) {
        builder.append(getLeafValueString(data.getAttribute(), data.getValue()));
    }

    @Override
    protected String getAttributesElementString(String attribute, String value) {
        return String.format(" %s=\"%s\"", attribute, value);
    }

    @Override
    protected void generateEndOfParentAttribute(ParentElement data, int indentationLevel) {
        builder.append(">\n");
    }

    @Override
    protected void generateEndOfParent(ParentElement data, int indentationLevel) {
        builder.append(INDENT.repeat(indentationLevel))
                .append(String.format("</%s>%n", data.getAttribute()));
    }

    @Override
    protected void generateAttributes(DataStructureElement data, int indentationLevel) {
        builder.append(INDENT.repeat(indentationLevel)).append(String.format("<%s", data.getAttribute()));
        if (data.getAttributeElements() != null) {
            for (LeafElement element : data.getAttributeElements()) {
                builder.append(getAttributesElementString(element.getAttribute(), element.getValue()));
            }
        }
    }

    private String getLeafValueString(String attribute, String value) {
        return value == null ? "/>\n" : String.format(">%s</%s>%n", value, attribute);
    }
}


