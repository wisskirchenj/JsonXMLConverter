package converter.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XMLParser objects contain all logic to parse string input in XML into abstract nested
 * data structures of attribute:value.
 */
public class XMLParser {
    private final String input;
    private final Pattern XML_ROOT_PATTERN = Pattern.compile("(?s)<(\\w+)(.*)(/>|</\\1>)");
    private final Pattern XML_LEAF_PATTERN = Pattern.compile("(?s)>([^<]*)|([^><\"]*)");
    private final Pattern XML_ATTRIBUTES_PATTERN = Pattern.compile("(\\w+)\\s*=\\s*\"(.*?[^\\\\])\"\\s*");

    public XMLParser(String userInput) {
        input = userInput;
    }

    /**
     * entry point for controller to parse the (complete) user input.
     * results are stored in model objects
     * @return the parsed data as abstract DataStructureElement
     */
    public DataStructureElement parse() {

        Matcher matcher = XML_ROOT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new JsonXMLParseException("XML parser: invalid XML-format found!");
        }
        String attribute = matcher.group(1);
        String value = matcher.group(2);

        DataStructureElement parsedData = parseForLeafElement(attribute, value);
        if (parsedData != null) {
            return parsedData;
        }
        parsedData = parseForLeafAttributesElement(attribute, value);
        if (parsedData != null) {
            return parsedData;
        }
        throw new JsonXMLParseException("XML parser: no child-elements supported in this version yet!");
    }

    private DataStructureElement parseForLeafElement(String attribute, String value) {
        Matcher matcher = XML_LEAF_PATTERN.matcher(value);
        if (matcher.matches()) {
            return new LeafElement(attribute,
                    matcher.group(2) == null ? matcher.group(1) : matcher.group(2));
        }
        return null;
    }

    private DataStructureElement parseForLeafAttributesElement(String attribute, String value) {
        Matcher matcher = XML_ATTRIBUTES_PATTERN.matcher(value);
        Iterator<MatchResult> matchesIterator = matcher.results().iterator();
        /* if (!matchesIterator.hasNext()) {
            //return null; in future stages
        }*/
        List<LeafElement> attributeElements = new ArrayList<>();
        MatchResult result = null;
        while (matchesIterator.hasNext()) {
            result = matchesIterator.next();
            attributeElements.add(new LeafElement(result.group(1), result.group(2)));
        }
        if (result == null || value.length() > result.end() && value.charAt(result.end()) != '>') {
            throw new JsonXMLParseException("XML parser: unsupported format yet!");
        }
        return new LeafAttributesElement(attribute, attributeElements,
                value.length() > result.end() + 1 ? value.substring(result.end() + 1) : "");
    }
}
