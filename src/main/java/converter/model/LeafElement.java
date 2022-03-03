package converter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a data leaf element, i.e. an element, that has no child elements.
 * It consists of an attribute-value pair, which are both represented by strings.
 * An empty value string represents the Json-null value.
 * Further it contains a (possibly null) list of attribute elements (which are itself LeafElement's)
 */
public class LeafElement implements DataStructureElement {

    private String attribute= null;
    private List<LeafElement> attributeElements;
    private final String value;

    public LeafElement(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public LeafElement(String value) {
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
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public void addAttributeElement(LeafElement element) {
        if (attributeElements == null) {
            attributeElements = new ArrayList<>();
        }
        attributeElements.add(element);
    }

    @Override
    public List<LeafElement> getAttributeElements() {
        return attributeElements;
    }
}
