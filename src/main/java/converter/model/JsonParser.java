package converter.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JsonParser objects contain all logic to parse string input in Json-format into abstract nested
 * data structures of attribute:value.
 */
public class JsonParser {
    private final String input;
    private final Pattern JSON_SINGLE_ROOT_PATTERN
            = Pattern.compile("(?s)\\{\\s*\"(\\w+)\"\\s*:\\s*(.*)\\s*}");
    private final Pattern SINGLE_STRING_PATTERN = Pattern.compile("\".*?[^\\\\]\"");
    private final Pattern SINGLE_NUMBER_PATTERN
            = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?" );
    private final Pattern SINGLE_BOOLEAN_PATTERN = Pattern.compile("true|false" );
    private final Pattern JSON_ATTRIBUTE_PATTERN = Pattern.compile(
            "(?s)\"([@,#])(\\w+)\"\\s*:\\s*(\"([^\"]*?[^\\\\])\"|\\S+)\\s*([,}])");

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

        DataStructureElement parsedData = parseForLeafElement(attribute, value);
        if (parsedData != null) {
            return parsedData;
        }
        parsedData = parseForLeafAttributesElement(attribute, value);
        if (parsedData != null) {
            return parsedData;
        }
        throw new JsonXMLParseException("Json parser: invalid Json-format found!");
    }

    /**
     * this private method has gotten a bit messy, because the parsing of nested
     * Json equivalent structures to XML-elements with attributes
     * (paired, unpaired, different Json data types ..) is complicated
     *      -> SHOULD BE REFACTORED FOR MAINTAINABILITY...
     * @param attribute the root attribute for the nested element -  as matched by the regexp
     * @param value the string which may (!) contain the remaining entries for this element
     * @return a generic generic key - attributes - value LeafAttributesElement, if this can be parsed
     *         or null if not - then other parsing continues in caller (or format-Exception).
     */
    private LeafAttributesElement parseForLeafAttributesElement(String attribute, String value) {
        Matcher matcher = JSON_ATTRIBUTE_PATTERN.matcher(value);
        Iterator<MatchResult> matchesIterator = matcher.results().iterator();
        /* if (!matchesIterator.hasNext()) {
            //return null; in future stages
        }*/
        List<LeafElement> attributeElements = new ArrayList<>();
        MatchResult result = null;
        String attributeValue= null;
        while (matchesIterator.hasNext()) {
            result = matchesIterator.next();
            attributeValue = result.group(3).contains("\"") ? result.group(4)
                    : result.group(3).equals("null") ? "" : result.group(3);
            if (result.group(1).equals("#")) {
                break;
            }
            attributeElements.add(new LeafElement(result.group(2), attributeValue));
        }
        if (result == null || !result.group(1).equals("#") || !result.group(2).equals(attribute)
                || result.end() != value.length() || !result.group(5).equals("}")) {
            throw new JsonXMLParseException("Json parser: unsupported format yet!");
        }
        return new LeafAttributesElement(attribute, attributeElements, attributeValue);
    }

    private LeafElement parseForLeafElement(String attribute, String value) {
        if ("null".equals(value)) {
            return new LeafElement(attribute, "");
        }
        if (SINGLE_STRING_PATTERN.matcher(value).matches()) {
            // remove surrounding quotes and replace escape '\' for inner quotes in value string.
            return new LeafElement(attribute,
                    value.substring(1, value.length() - 1).replaceAll("\\\\\"", "\""));
        }
        if (SINGLE_BOOLEAN_PATTERN.matcher(value).matches()
                || SINGLE_NUMBER_PATTERN.matcher(value).matches()) {
            return new LeafElement(attribute, value);
        }
        return null;
    }
}
