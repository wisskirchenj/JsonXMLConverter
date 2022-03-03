package converter.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XMLParser objects contain all logic to parse string input in XML into abstract nested
 * data structures of attribute:value.
 * Implementation here is rather complex - by nature of the task - but is already
 * refactored, completely working and tested and near final.
 */
public class XMLParser {

    private final Pattern XML_UNPAIRED_ELEMENT_PATTERN = Pattern.compile("(?s)<([^>]+)/>\\s*");
    private final Pattern XML_PAIRED_ELEMENT_PATTERN = Pattern.compile("(?s)<(([^>]+)[^>]*)>?(.*?)</\\2>\\s*");
    private final Pattern XML_TAG_PATTERN = Pattern.compile("(\\w+)\\s*(.*?)\\s*");
    private final Pattern XML_ATTRIBUTE_PATTERN = Pattern.compile("(\\w+)\\s*=\\s*\"(.*?[^\\\\])\"\\s*");

    /**
     * central parse method entry point for this class, that is also called recursively with
     * smaller sub parts of the input string given
     * Central ingredient are the matcher patterns of UNPAIRED and PAIRED XML elements
     * @param input at first call the complete user input - typically given by a file read
     *              smaller parts in recursion calls
     * @return generic data in form of a DataStructureElement
     */
    public DataStructureElement parse(String input) {

        Matcher matcher = XML_UNPAIRED_ELEMENT_PATTERN.matcher(input);
        if (matcher.find() && matcher.start() == 0) {
            // first recursion base case
            LeafElement parsedData = new LeafElement(null);
            parseAttributes(parsedData, matcher.group(1));
            return parsedData;
        }

        matcher = XML_PAIRED_ELEMENT_PATTERN.matcher(input);
        if (!matcher.find() || matcher.start() != 0) {
            throw new JsonXMLParseException("XML parser: invalid format found!");
        }
        String attribute = matcher.group(1);
        String value = matcher.group(3);

        // second recursion base case
        if (!isNested(value)) {
            LeafElement parsedData = new LeafElement(value);
            parseAttributes(parsedData, attribute);
            return parsedData;
        }

        ParentElement parsedData = new ParentElement();
        parseAttributes(parsedData, attribute);

        value = value.trim();
        int indexShift = 0; // keep track, that whole input is matched
        do {
            matcher = XML_UNPAIRED_ELEMENT_PATTERN.matcher(value);
            if (!matcher.find(indexShift) || matcher.start() != indexShift) {
                matcher = XML_PAIRED_ELEMENT_PATTERN.matcher(value);
                if (!matcher.find(indexShift) || matcher.start() != indexShift) {
                    throw new JsonXMLParseException("XML parser: invalid format found!");
                }
            }
            // reduction step of recursion
            parsedData.addValueElement(parse(value.substring(matcher.start(), matcher.end()).trim()));
            indexShift = matcher.end();
        } while (matcher.end() < value.length());

       return parsedData;
    }

    /**
     * parse an attribute string with tag name and attributes list as extracted by the pattern's
     * of the parse-method.
     * @param dataStructure the DataStructureElement, where the parsing gets filled into
     * @param attribute the string, that is to be parsed
     */
    protected void parseAttributes(DataStructureElement dataStructure, String attribute) {
        Matcher matcher = XML_TAG_PATTERN.matcher(attribute);
        if (!matcher.matches()) {
            throw new JsonXMLParseException("XML parser: unsupported format yet!");
        }
        dataStructure.setAttribute(matcher.group(1));
        String attributeList = matcher.group(2);
        if (attributeList == null) {
            return;
        }
        matcher = XML_ATTRIBUTE_PATTERN.matcher(attribute);
        while (matcher.find()) {
            dataStructure.addAttributeElement(new LeafElement(matcher.group(1), matcher.group(2)));
        }
    }

    protected boolean isNested(String value) {
        return value.contains("<");
    }
}
