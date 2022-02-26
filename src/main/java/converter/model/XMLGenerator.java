package converter.model;

/**
 * converts an abstract data structure attribute:value into text-lines in XML-format.
 */
public class XMLGenerator {
    DataStructureElement data;

    public XMLGenerator(DataStructureElement dataStructure) {
        data = dataStructure;
    }
    /**
     * entry point to start generation of Json-format
     * @return lines of Json-Code
     */
    public String generate() {
        return "to be implemented";
    }
}


