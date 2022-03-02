package converter.model;

import java.util.List;

/**
 * Class representing a data leaf element with attributes and a possible element value string
 * Thus it represents a paired or unpaired XML-tag with attributes and (possibly empty) content.
 * The String content is accessed by the interface getter getValue(), while the attributes-list
 * is accessed via the additional method getAttributeElements().
 */
public class LeafAttributesElement implements DataStructureElement {

    private final String attribute;
    private final List<LeafElement> attributeElements;
    private final String value;

    public LeafAttributesElement(String attribute, List<LeafElement> attributeElements, String value) {
        this.attribute = attribute;
        this.attributeElements = attributeElements;
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

    public List<LeafElement> getAttributeElements() {
        return attributeElements;
    }
}
