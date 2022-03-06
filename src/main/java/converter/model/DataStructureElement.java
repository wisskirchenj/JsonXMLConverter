package converter.model;

import java.util.List;

/**
 * implementations of this interface represent an abstract attribute:value
 * data structure, where the value can be nested.
 * implementing classes should consist of at least an
 * - attribute string field named "attribute"
 * - attributeElements field named "attributeElements" of type List<LeafElement>
 * - value Element field named "value", which can be of several types,
 *      e.g. String or List<DataStructureElements> - but also Integer, Date etc.
 */
public interface DataStructureElement {

    String getAttribute();

    void setAttribute(String attribute);

    List<LeafElement> getAttributeElements();

    /**
     * adds a LeafElement representing an attribute-value pair to the attributeElements list
     * @param element a Leafelement to be added to the list attributeElements
     */
    void addAttributeElement(LeafElement element);

    /**
     * getter vor value of generic type Object. The value is given on construction for LeafElement
     * or by a non-interface method addValueElement() in case of ParentElement implementation
     * -> thus no setter needed presently, also to avoid casting...
     * @return the value as Object - implementations specify the return type further
     */
    Object getValue();
}
