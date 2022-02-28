package converter.model;

/**
 * converts an abstract data structure attribute:value into text-lines in XML-format.
 */
public class XMLGenerator extends Generator {

    @Override
    protected String getFinalText() {
        return "";
    }

    @Override
    protected String getInitialText() {
        return "";
    }

    @Override
    protected void generateLeafAttributesElement(LeafAttributesElement data, int indentationLevel) {
        for (LeafElement element : data.getAttributeElements()) {
            builder.append(getAttributesElementString(element.getAttribute(), element.getValue()));
        }
        builder.append(getLeafValueString(data.getAttribute(), data.getValue()));
    }

    @Override
    protected String getAttributeString(String attribute) {
        return String.format("<%s", attribute);
    }

    private String getAttributesElementString(String attribute, String value) {
        return String.format(" %s=\"%s\"", attribute, value);
    }

    @Override
    protected int getInitialIndentLevel() {
        return 0;
    }

    @Override
    protected String getLeafValueString(String attribute, String value) {
        return value.equals("") ? "/>\n" : String.format(">%s</%s>%n", value, attribute);
    }
}


