package converter.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class derived from the abstract class Tokenizer, that implement the tokenization
 * of Json-input.
 * Upon creation with a text to be parsed, the tokenizer object immediately tokenizes
 * and offers the result as Queue or List.
 */
public class JsonTokenizer extends Tokenizer {

    public JsonTokenizer(String text) {
        tokenizeInput(text);
    }

    /**
     * low level parse-logic, where one token of the input-string is parsed and returned.
     * A token is either a "key":value pair, separated by end of text or ',' - or it is
     * a nested structure, which is identified by counting how many open braces are closed again
     * and delimited again by ',' or text end.
     * @param text the text where the token is taken from the beginning
     * @return the token that exactly matches one Json-element of the list.
     */
    protected String getNextToken(String text) {
        Matcher matcher = Pattern.compile("[,{\\[]").matcher(text);
        if (!matcher.find()) {
            return text;
        }
        int pos = matcher.start();
        // next separator is a comma ? -> then use it
        if (text.charAt(pos) == ',') {
            return text.substring(0, pos);
        }
        // else we have to count until brace closes
        char open = text.charAt(pos);
        char close = open == '{' ? '}' : ']';

        int depth = 1;
        while (++pos < text.length() && depth > 0) {
            if (text.charAt(pos) == open) {
                depth++;
            } else if (text.charAt(pos) == close) {
                depth--;
            }
        }
        if (text.indexOf(',', pos) > 0) {
            return text.substring(0, text.indexOf(',', pos));
        }
        // if there is no comma, no new element must be present in the text ...
        if (text.indexOf('{', pos) > 0 || text.indexOf(':', pos) > 0) {
            throw new JsonXMLParseException("Json-Parser: Invalid format found!");
        }
        return text;
    }


    /**
     * this overridden (in Tokenizer empty implemented) method is used to modify this token by inserting "element":
     * keys before each array value, so that the subsequent parsing of the contents can be performed as if there
     * were a standard Json child element's list.
     */
    @Override
    // This code parses an array token into a JSON object. For example, the
    // token "[ 1, 2, 3 ]" is parsed into the object "{ element: 1, element: 2,
    // element: 3 }".

    protected String modifyArrayToken(String arrayToken) {
        // If the array is empty, return an empty string.
        if (arrayToken.matches("(?s)\\[\\s*]")) {
            return "\"\"";
        }

        // Create a StringBuilder to accumulate the JSON object.
        StringBuilder builder = new StringBuilder("{ ");

        // Parse the array token into elements.
        int position = 1;
        String elementToken;
        do {
            // Get the next element token.
            elementToken = getNextToken(arrayToken.substring(position, arrayToken.length() - 1));

            // Update the position in the array token.
            position += elementToken.length() + 1;

            // Append the element token to the JSON object.
            builder.append("\"element\": ").append(elementToken).append(",\n");
        } while (position < arrayToken.length() - 1);

        // Finish the JSON object.
        builder.append("}");

        // Return the JSON object.
        return builder.toString();
    }

}
