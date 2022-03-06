package converter.model;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JsonParser objects contain all logic to parse string input in Json-format into abstract nested
 * data structures of attribute:value.
 * TO BE REFACTORED in next stage(s) !!! It uses the obsolete LeafAttributesElement - class
 * and also cannot handle well recursive structures yet...
 */
public class JsonParser {

    private final Pattern SINGLE_ROOT_PATTERN
            = Pattern.compile("(?s)\"([.\\w]*)\"\\s*:\\s*\\{(.*)}\\s*");
    private final Pattern SINGLE_NON_EMPTY_STRING_PATTERN
            = Pattern.compile("(?s)\"([.\\w]*)\"\\s*:\\s*\"(.*?[^\\\\])\"");
    private final Pattern SINGLE_EMPTY_STRING_PATTERN = Pattern.compile("(?s)\"(\\w*)\"\\s*:\\s*\"\"");
    private final Pattern SINGLE_NUMBER_PATTERN
            = Pattern.compile("(?s)\"([.\\w]*)\"\\s*:\\s*([-+]?[0-9]*\\.?[0-9]+)");
    private final Pattern SINGLE_BOOLEAN_PATTERN
            = Pattern.compile("(?s)\"([.\\w]*)\"\\s*:\\s*(true|false)");
    private final Pattern SINGLE_NULL_PATTERN
            = Pattern.compile("(?s)\"([.\\w]*)\"\\s*:\\s*null");
    private final Pattern JSON_ATTRIBUTE_PATTERN
            = Pattern.compile("(?s)\"[@#]([.\\w]+)\"\\s*:\\s*(\"([^\"]*?)\"|[^\\s{]+)");
    private final Pattern JSON_CHILD_TOKEN_PATTERN = Pattern.compile("(?s)\"([@#]?[.\\w]*)\".*");

    /**
     * entry point for controller to parse the (complete) user input.
     * results are stored in model objects
     * @return the parsed data as abstract DataStructureElement
     */
    public DataStructureElement parse(String input) {
        if (!input.endsWith("}")) {
            throw new JsonXMLParseException(
                    "Json parser: invalid Json-format found (array as root element not supported yet)!");
        }
        ParentElement rootElement = new ParentElement();
        // cut off enclosing braces and whitespace and parse
        parseObjectsList(rootElement, input.substring(1, input.length() - 1).trim());
        if (rootElement.getValue().size() == 1) {
            return rootElement.getValue().get(0);
        }
        rootElement.setAttribute("root");
        return rootElement;
    }

    /**
     * central recursive method of this parser, that does the scanning of the child list of a
     * Json object (i.e. anything embraced). Method is called, after the attribute of the parent has
     * been parsed before and a parent element created, which is given as parameter and to be filled
     * with values by this method.
     * @param parent the parent element, the object list to be parsed belongs to
     * @param input the data string to be parsed
     */
    private void parseObjectsList(ParentElement parent, String input) {
        Deque<String> tokens = tokenizeInput(input);
        if (parent.getAttribute() != null) { // is only null for the rootElement
            // find and assign possible attributes to this parent element
            parseListForAttributes(parent, tokens);
        }
        for (String token : tokens) {

            // recursion base step
            LeafElement parsedLeaf = parseTokenForLeafElement(token);
            if (parsedLeaf != null) {
                if (!parsedLeaf.getAttribute().isEmpty()) {
                    parent.addValueElement(parsedLeaf);
                }
                continue;
            }

            ParentElement parsedObject = parseParentObject(token);
            parent.addValueElement(parsedObject);
            // reduction step of recursion
            parseObjectsList(parsedObject,
                    token.substring(token.indexOf('{') + 1, token.lastIndexOf('}')).trim());
        }
    }

    /**
     * method called after an unsuccessful attempt to parse the object for a leaf element
     * thus, at this point it is clear, that the value is a nested Json-object.
     * the method thus creates a parent element and sets its parsed attribute
     * @param token the string data of this parent object
     * @return the created parent object with attribute set.
     */
    private ParentElement parseParentObject(String token) {
        Matcher matcher = SINGLE_ROOT_PATTERN.matcher(token);
        if (!matcher.matches()) {
            throw new JsonXMLParseException("Json-Parser: Invalid Format");
        }
        ParentElement element = new ParentElement();
        element.setAttribute(matcher.group(1));
        return element;
    }

    /**
     * method gets an arbitrary object and attempts to parse it for a leaf element structure,
     * which may either be a key:value pair with value of data type string, number, boolean or null
     * or a nested object, where all child-elements satisfy an attribute-value pattern, which
     * then represents an XML-element with attributes and (possibly empty) value.
     * @param token the string data of this parent object
     * @return the created leaf object with attribute set - or null, if the objects value is nested
     */
    private LeafElement parseTokenForLeafElement(String token) {

        Matcher matcher = SINGLE_NON_EMPTY_STRING_PATTERN.matcher(token);
        if (matcher.matches()) {
            return new LeafElement(matcher.group(1), matcher.group(2));
        }
        matcher = SINGLE_EMPTY_STRING_PATTERN.matcher(token);
        if (matcher.matches()) {
            return new LeafElement(matcher.group(1), "");
        }
        matcher = SINGLE_NULL_PATTERN.matcher(token);
        if (matcher.matches()) {
            return new LeafElement(matcher.group(1), null);
        }
        matcher = SINGLE_BOOLEAN_PATTERN.matcher(token);
        if (matcher.matches()) {
            return new LeafElement(matcher.group(1), matcher.group(2));
        }
        matcher = SINGLE_NUMBER_PATTERN.matcher(token);
        if (matcher.matches()) {
            return new LeafElement(matcher.group(1), matcher.group(2));
        }

        matcher = SINGLE_ROOT_PATTERN.matcher(token);
        if (!matcher.matches()) {
            throw new JsonXMLParseException("Json-Parser: Invalid format found!");
        }
        LeafElement leafElement = new LeafElement(matcher.group(1), null);
        Deque<String> tokens= tokenizeInput(matcher.group(2));
        if (parseListForAttributes(leafElement, tokens)) {
            leafElement.setValue(parseTokenForLeafElement(tokens.poll()).getValue());
            return leafElement;
        }
        return null;
    }

    /**
     * checks, if the structure has a valid @att1:val1, ..., @att2:val2, #attribute=object_value structure.
     * If this structure is found, all tokens are removed and only the object_value is offered as single
     * new element into the queue.
     * @param element the data structure element, that consists of the content of this tokensQueue
     * @param tokensQueue queue of tokens to be parsed for attribute structure
     * @return true, if the tokens queue matches a valid single (!) XML attributes pattern
     */
    private boolean parseListForAttributes(DataStructureElement element, Deque<String> tokensQueue) {

        String valueKey = String.format("\"#%s\"", element.getAttribute());
        if (isValidAttributesList(tokensQueue, valueKey)) {
            return moveAttributesToElement(element, (LinkedList<String>) tokensQueue);
        } else {
            repairChildList((LinkedList<String>) tokensQueue);
            // empty child elements counts as empty value with key = tag_name
            if (tokensQueue.isEmpty()) {
                tokensQueue.offer("\"" + element.getAttribute() +"\" :\"\"");
                return true;
            }
        }
        return false;
    }

    /**
     * this method is called, if the child-list does NOT fully match the attributes-value
     * syntax. It applies some rules given by the specification, that attribute-marked
     * tags are modified to normal child elements, if the whole syntax does not match.
     * also in this case no two child-attributes with same name are allowed.
     * in this case, the one is taken without "@" or "#"...
     * @param tokensList list of tokens after the negative attribute match -> the list is modified !
     */
    private void repairChildList(LinkedList<String> tokensList) {
        Set<String> keySet = new HashSet<>();
        ListIterator<String> iterator = tokensList.listIterator(tokensList.size());
        while (iterator.hasPrevious()) {
            String token = iterator.previous();
            Matcher matcher = JSON_CHILD_TOKEN_PATTERN.matcher(token);
            if (!matcher.matches() || matcher.group(1).isEmpty() || "@".equals(matcher.group(1)) ||
                    ("#".equals(matcher.group(1)))) {
                iterator.remove();
                continue;
            }
            if (matcher.group(1).startsWith("@") || matcher.group(1).startsWith("#")) {
                iterator.set("\"" + token.substring(2));
                if (keySet.contains(matcher.group(1).substring(1))) {
                    iterator.remove();
                }
            } else {
                keySet.add(matcher.group(1));
            }
        }
    }

    /**
     * this method is called, if the child-list does match a valid attributes and
     * value structure of an XML-element. In this case all the attributes are added to the
     * given data structure element's attributesList.
     * @param element a data structure element, where the attributes are added.
     * @param tokensList the tokens list, that matches to attributes key-value pairs and
     *                   the element value itself.
     * @return true, if the value of the element "#tag_name" is a single string and can
     *          thus be processed further into a LeafElement, false if value is nested itself.
     */
    private boolean moveAttributesToElement(DataStructureElement element, LinkedList<String> tokensList) {

        Iterator<String> iterator = tokensList.iterator();
        String valueToken = null;
        while (iterator.hasNext()) {
            String token = iterator.next();
            if (token.startsWith("\"@")) {
                Matcher matcher = JSON_ATTRIBUTE_PATTERN.matcher(token);
                matcher.matches(); // clear at this point, that it does
                String attributeValue = matcher.group(3) != null ? matcher.group(3) : matcher.group(2);
                if (attributeValue.equals("null")) {
                    attributeValue = "";
                }
                element.addAttributeElement(new LeafElement(matcher.group(1), attributeValue));
                iterator.remove();
            }
            if (token.startsWith("\"#")) {
                valueToken = token;
                if (token.contains("{")) {
                    iterator.remove();
                }
            }
        }
        if (valueToken.contains("{")) {
            tokensList.addAll(tokenizeInput(valueToken
                    .substring(valueToken.indexOf('{')+1, valueToken.lastIndexOf('}')).trim()));
            return false;
        }
        tokensList.set(0, "\"" + valueToken.substring(2));
        return true;
    }

    /**
     * this method applies all specified checks for a child-list, if it matches a valid
     * XML-structure with attributes and value. To be this, all child-elements must either
     * be valid key:string-value pairs for attributes and start with "@" or be the one and
     * only value-attribute, that must have a key "#<tag_name>".
     * @param tokensList the token's collection to be parsed
     * @param valueKey the value-key for this XML-element ("#<tag_name>")
     * @return true, if the child-list matches the attributes structure (with possibly nested value)
     *         false else
     */
    private boolean isValidAttributesList(Deque<String> tokensList, String valueKey) {
        boolean isAttributesList = true;
        boolean valueKeyFound = false;
        for (String token : tokensList) {
            if (token.startsWith(valueKey)) {
                valueKeyFound = true;
                continue;
            }
            Matcher matcher = JSON_ATTRIBUTE_PATTERN.matcher(token);
            if (!matcher.matches()) {
                isAttributesList = false;
                break;
            }
        }
        return isAttributesList && valueKeyFound;
    }

    /**
     * entry point for a rather complicated parsing of a string, that comprises all data of an
     * arbitrary Json-object (even the root-element in first call) into a list of tokens, that
     * precisely contain the data of one child-element. It has a sub call to a method matching one next token.
     * @param input the input string containing all object's data
     * @return an Array-Deque of the tokens
     */
    private Deque<String> tokenizeInput(String input) {
        Deque<String> tokens = new LinkedList<>();
        int position = 0;
        String token;
        do {
            token = getNextToken(input.substring(position));
            position += token.length() + 1;
            tokens.offer(token.trim());
        } while (position < input.length());
        return tokens;
    }

    /**
     * low level parse-logic, where one token of the input-string is parsed and returned.
     * A token is either a "key":value pair, separated by end of text or ',' - or it is
     * a nested structure, which is identified by counting how many open braces are closed again
     * and delimited again by ',' or text end.
     * @param text the text where the token is taken from the beginning
     * @return the token that exactly matches one Json-element of the list.
     */
    private String getNextToken(String text) {
        int end = text.indexOf('{');
        if (end < 0 || end > text.indexOf(',')) {
            return text.indexOf(',') < 0 ? text : text.substring(0, text.indexOf(','));
        }
        int depth = 1;
        while (++end < text.length() && depth > 0) {
            if (text.charAt(end) == '{') {
                depth++;
            } else if (text.charAt(end) == '}') {
                depth--;
            }
        }
        if (text.indexOf(',', end) > 0) {
            return text.substring(0, text.indexOf(',', end));
        }
        if (text.indexOf('{', end) > 0 || text.indexOf(':', end) > 0) {
            throw new JsonXMLParseException("Json-Parser: Invalid format found!");
        }
        return text;
    }
}