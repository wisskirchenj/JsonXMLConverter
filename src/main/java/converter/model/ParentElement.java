package converter.model;

import java.util.List;

/**
 * Class representing a data element with a structured value, i.e. an element,
 * that has child elements in Json and child tags or attributes in HTML.
 * It consists of an attribute-value pair, where attribute is represented by a string
 * and the value is a List of DataStructureElements (possibly empty or with one entry)
 */
public class ParentElement implements DataStructureElement {

    private final String attribute;
    private final List<DataStructureElement> value;

    public ParentElement(String attribute, List<DataStructureElement> valueList) {
        this.attribute = attribute;
        this.value = valueList;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    @Override
    public List<DataStructureElement> getValue() {
        return value;
    }
}
