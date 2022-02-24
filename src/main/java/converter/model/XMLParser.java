package converter.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XMLParser objects contain all logic to parse string input in XML into abstract nested
 * data structures of attribute:value.
 */
public class XMLParser {

    // static to avoid compiling for any instance.
    private static final Pattern PAIRED_XML_ROOT_PATTERN = Pattern.compile("<(\\w+)>(.*)</\\1>");
    private static final Pattern XML_ELEMENT_WITH_TAGS_PATTERN
            = Pattern.compile("(\\s*<(\\w+)>.*</\\2>\\s*|\\s*<\\w+/>\\s*)+");

    private final String input;

    public XMLParser(String userInput) {
        input = userInput;
    }

    /**
     * entry point for controller to parse the (complete) user input.
     * results are stored in model objects
     * @return the parsed data as abstract DataStructureElement
     */
    public DataStructureElement parse() {
        // single unpaired XML tag ?
        if (input.matches("<\\w+/>")) {
            return new LeafElement(input.substring(1, input.length() - 2), "");
        }

        Matcher matcher = PAIRED_XML_ROOT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new JsonXMLParseException("XML parser: invalid XML-format found!");
        }
        String attribute = matcher.group(1);
        String value = matcher.group(2);

        if (XML_ELEMENT_WITH_TAGS_PATTERN.matcher(value).matches()) {
            throw new JsonXMLParseException("XML parser: no child-elements supported in this version yet!");
        }
        return new LeafElement(attribute, value);
    }
}
