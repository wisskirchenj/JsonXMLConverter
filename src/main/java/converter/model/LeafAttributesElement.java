package converter.model;

import java.util.List;

/**
 * Legacy - code... OBSOLETE !
 * -> will get removed as soon as JsonParser is refactored...
 * There is no longer a difference between LeafElement and LeafAttributesElements (with attributes)
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
    public void setAttribute(String attribute) {

    }

    @Override
    public void addAttributeElement(LeafElement element) {

    }

    @Override
    public String getValue() {
        return value;
    }

    public List<LeafElement> getAttributeElements() {
        return attributeElements;
    }
}
