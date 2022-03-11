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

    private final Pattern xmlPrologPattern = Pattern.compile("(?s)<\\?.*?\\?>\\s*");
    private final Pattern xmlTagPattern = Pattern.compile("(\\w+)\\s*(.*?)\\s*");
    private final Pattern xmlAttributePattern = Pattern.compile("(\\w+)\\s*=\\s*[\"'](.*?)[\"']\\s*");
    private final XMLTokenizer tokenizer = new XMLTokenizer();

    /**
     * central parse method entry point for this class, that is also called recursively with
     * smaller sub parts of the input string given
     * Central ingredient are the matcher patterns of UNPAIRED and PAIRED XML elements
     * @param input at first call the complete user input - typically given by a file read
     *              smaller parts in recursion calls
     * @return generic data in form of a DataStructureElement
     */
    public DataStructureElement parse(String input) {

        input = cutProlog(input);

        String nextElementToken = tokenizer.getNextToken(input);
        LeafElement parsedData = parseForUnpairedLeaf(nextElementToken);
        if (parsedData != null) {
            // first recursion base case
            return parsedData;
        }

        Matcher matcher = tokenizer.getPairedPattern().matcher(nextElementToken);
        if (!matcher.matches()) {
            throw new JsonXMLParseException("cannot happen :-)");
        }
        String attribute = matcher.group(1);
        String value = matcher.group(3);

        if (!isNested(value)) {
            // second recursion base case
            return parseForPairedLeaf(attribute, value);
        }

        // inside the following method is the reduction step of the recursion
        return parseForParent(attribute, value);
    }

    /**
     * if XML-input has a prolog tag <?something?> at the beginning (which is the only place, where the
     * XML-syntax allows it), this is removed from the input by this method.
     * @param input the XML-text to parse
     * @return a XML-prolog free text.
     */
    private String cutProlog(String input) {
        Matcher matcher = xmlPrologPattern.matcher(input);
        if (matcher.find() && matcher.start() == 0) {
            return input.substring(matcher.end()).trim();
        }
        return input;
    }

    /**
     * parse input value text to match the value of a paired nested XML-element (a ParentElement)
     * @param attribute already parsed attribute name given by caller
     * @param value value-text to be parsed
     * @return the created and filled ParentElement, if text started with a match for a paired
     * XML-element. If not, an exception is thrown.
     */
    private DataStructureElement parseForParent(String attribute, String value) {
        ParentElement parsedData = new ParentElement();
        parseAttributes(parsedData, attribute);

        value = value.trim();
        int indexShift = 0; // keep track, that whole input is matched
        do {
            String nextElementToken = tokenizer.getNextToken(value.substring(indexShift));

            // reduction step of recursion
            parsedData.addValueElement(parse(nextElementToken.trim()));
            indexShift += nextElementToken.length();
        } while (indexShift < value.length());

        return parsedData;
    }

    /**
     * parse input text to see, if value text matches with a paired XML-element (i.e. not nested)
     * @param attribute already parsed attribute name given by caller
     * @param value value-text to be parsed
     * @return th created and filled LeafElement, if text started with a match for a paired
     * XML-element, null else.
     */
    private DataStructureElement parseForPairedLeaf(String attribute, String value) {
        LeafElement parsedData = new LeafElement(value);
        parseAttributes(parsedData, attribute);
        return parsedData;
    }

    /**
     * parse input text to see, if text starts with a unpaired XML-leafElement (i.e. not nested)
     * @param input text to be parsed
     * @return th created and filled LeafElement, if text started with a match for an unpaired
     * XML-element, null else.
     */
    private LeafElement parseForUnpairedLeaf(String input) {
        Matcher matcher = tokenizer.getUnpairedPattern().matcher(input);
        if (matcher.find() && matcher.start() == 0) {
            LeafElement parsedData = new LeafElement(null);
            parseAttributes(parsedData, matcher.group(1));
            return parsedData;
        }
        return null;
    }

    /**
     * parse an attribute string with tag name and attributes list as extracted by the pattern's
     * of the parse-method.
     * @param dataStructure the DataStructureElement, where the parsing gets filled into
     * @param attribute the string, that is to be parsed
     */
    protected void parseAttributes(DataStructureElement dataStructure, String attribute) {
        Matcher matcher = xmlTagPattern.matcher(attribute);
        if (!matcher.matches()) {
            throw new JsonXMLParseException("XML parser: unsupported format yet!");
        }
        dataStructure.setAttribute(matcher.group(1));
        String attributeList = matcher.group(2);
        if (attributeList == null) {
            return;
        }
        matcher = xmlAttributePattern.matcher(attribute);
        while (matcher.find()) {
            dataStructure.addAttributeElement(new LeafElement(matcher.group(1), matcher.group(2)));
        }
    }

    protected boolean isNested(String value) {
        return value.contains("<");
    }
}
