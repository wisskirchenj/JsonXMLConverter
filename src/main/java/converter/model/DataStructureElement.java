package converter.model;

/**
 * implementations of this interface represent an abstract attribute:value
 * data structure, where the value can be nested.
 */
public interface DataStructureElement {

    /**
     * Getter
     * @return attribute string
     */
    String getAttribute();

    /**
     * Getter
     * @return value object - typically a collection of (or a single) DataStructureElement(s)
     *          or a String in case of a LeafElement.
     */
    Object getValue();

    /**
     * convert data structure to lines of XML-code.
     * @return XML - lines
     */
    String[] toXML();

    /**
     * convert data structure to lines of a Json-obejct.
     * @return Json-format - lines
     */
    String[] toJson();
}
