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
        super();
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
        // next separator is a comma ? -> then use it
        if (text.charAt(matcher.start()) == ',') {
            return text.substring(0, matcher.start());
        }
        // else we have to count until brace closes
        char open = text.charAt(matcher.start()) == '{' ? '{' : '[';
        char close = open == '{' ? '}' : ']';

        int pos = matcher.start();
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
    protected String modifyArrayToken(String arrayToken) {
        if (arrayToken.matches("(?s)\\[\\s*]")) {
            return "\"\"";
        }
        StringBuilder builder = new StringBuilder("{ ");
        int position = 1;
        String elementToken;
        do {
            elementToken = getNextToken(arrayToken.substring(position, arrayToken.length() - 1));
            position += elementToken.length() + 1;

            builder.append("\"element\": ").append(elementToken).append(",\n");
        } while (position < arrayToken.length() - 1);
        builder.append("}");
        return builder.toString();
    }

}
