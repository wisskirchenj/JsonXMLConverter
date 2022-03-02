package converter.model;

/**
 * Class representing a data leaf element, i.e. an element, that has no child elements.
 * It consists of an attribute-value pair, which are both represented by strings.
 * An empty value string represents the Json-null value.
 */
public class LeafElement implements DataStructureElement {

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
}
