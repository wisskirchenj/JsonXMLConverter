package converter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a data element with a structured value, i.e. an element,
 * that has child elements in Json and child tags or attributes in HTML.
 * It consists of an attribute-value pair, where attribute is represented by a string
 * and the value is a List of DataStructureElements (possibly empty or with one entry)
 * as well as a list of LeafElements representing the XML-attribute-value pairs.
 */
public class ParentElement implements DataStructureElement {

    private String attribute= null;
    private List<LeafElement> attributeElements = null;
    private List<DataStructureElement> value = null;

    @Override
    public String getAttribute() {
        return attribute;
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

    /**
     * method (in addition to the DataStructureElement - so variable has to be of this type or cast
     * to be used) that adds a DataStructureElement to the list of values.
     * Thus, the parent element can contain a nested list of child elements - which
     * form the value field of the interface.
     * @param element the value element to be added
     */
    public void addValueElement(DataStructureElement element) {
        if (value == null) {
            value = new ArrayList<>();
        }
        value.add(element);
    }

    @Override
    public List<DataStructureElement> getValue() {
        return value;
    }

    @Override
    public List<LeafElement> getAttributeElements() {
        return attributeElements;
    }
}
