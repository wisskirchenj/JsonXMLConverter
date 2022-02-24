package converter.model;

/**
 * JsonParser objects contain all logic to parse string input in Json-format into abstract nested
 * data structures of attribute:value.
 */
public class JsonParser {

    private final String input;

    public JsonParser(String userInput) {
        input = userInput;
    }

    /**
     * entry point for controller to parse the (complete) user input.
     * results are stored in model objects
     * @return the parsed data as abstract DataStructureElement
     */
    public DataStructureElement parse() {
        return new LeafElement("to be implemented", "");
    }
}
