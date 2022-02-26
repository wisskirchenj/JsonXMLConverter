package converter.model;

/**
 * Class representing a data leaf element, i.e. an element, that has no child elements.
 * It consists of an attribute-value pair, which are both represented by strings.
 * An empty value string represents the Json-null value.
 */
public class LeafElement implements DataStructureElement {

    private static final String NULL = "";

    private final String attribute;
    private final String value;

    public LeafElement(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String[] toXML() {
        String line = value.equals(NULL) ? String.format("<%s/>", attribute) :
                String.format("<%1$s>%2$s</%1$s>", attribute, value);
        return new String[] {line};
    }

    @Override
    public String[] toJson() {
        String line = value.equals(NULL) ? String.format("\"%s\": null", attribute) :
                String.format("\"%s\": \"%s\"", attribute,
                        value.replaceAll("\"", "\\\\\""));
        return new String[] {line};
    }

}
