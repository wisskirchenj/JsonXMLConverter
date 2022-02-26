package converter.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JsonParser objects contain all logic to parse string input in Json-format into abstract nested
 * data structures of attribute:value.
 */
public class JsonParser {
    private final String input;
    private final Pattern JSON_SINGLE_ROOT_PATTERN
            = Pattern.compile("\\{\\s*\"(\\w+)\"\\s*:\\s*(.*)\\s*}");
    private final Pattern SINGLE_STRING_PATTERN = Pattern.compile("\".*[^\\\\]+\"");
    private final Pattern SINGLE_NUMBER_PATTERN
            = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?" );
    private final Pattern SINGLE_BOOLEAN_PATTERN = Pattern.compile("true|false" );
    private final Pattern JSON_OBJECT_PATTERN = Pattern.compile("[{\\[]\\s*(\\S+)\\s*[}\\]]");


    public JsonParser(String userInput) {
        input = userInput;
    }

    /**
     * entry point for controller to parse the (complete) user input.
     * results are stored in model objects
     * @return the parsed data as abstract DataStructureElement
     */
    public DataStructureElement parse() {

        Matcher matcher = JSON_SINGLE_ROOT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new JsonXMLParseException(
                    "Json parser: invalid Json-format found (array as root element not supported yet)!");
        }
        String attribute = matcher.group(1);
        String value = matcher.group(2).trim();

        if ("null".equals(value)) {
            return new LeafElement(attribute, "");
        }

        if (SINGLE_STRING_PATTERN.matcher(value).matches()) {
            // remove surrounding quotes and replace escape '\' for inner quotes
            return new LeafElement(attribute,
                    value.substring(1, value.length() - 1).replaceAll("\\\\\"", "\""));
        }
        if (SINGLE_BOOLEAN_PATTERN.matcher(value).matches()
                || SINGLE_NUMBER_PATTERN.matcher(value).matches()) {
            return new LeafElement(attribute, value);
        }

        if (JSON_OBJECT_PATTERN.matcher(value).matches()) {
            throw new JsonXMLParseException("Json parser: no child-objects supported in this version yet!");
        }
        throw new JsonXMLParseException("Json parser: invalid Json-format found!");
    }
}
