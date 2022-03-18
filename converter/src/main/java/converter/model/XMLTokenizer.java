package converter.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class derived from the abstract class Tokenizer, that implement the tokenization
 * of XML-input.
 * Upon creation with a text to be parsed, the tokenizer object immediately tokenizes
 * and offers the result as Queue or List.
 * In present implementation the superclass queue or list are not used - but they can in the future.
 */
public class XMLTokenizer extends Tokenizer {

    private final Pattern xmlUnpairedElementPattern = Pattern.compile("(?s)<([^>]+)/>\\s*");
    private final Pattern xmlPairedElementPattern = Pattern.compile("(?s)<(([^>]+)[^>]*)>(.*?)</\\2>\\s*");

    /**
     * low level parse-logic, where one token of the input-string is parsed and returned.
     * A token is either a "key":value pair, separated by end of text or ',' - or it is
     * a nested structure, which is identified by counting how many open braces are closed again
     * and delimited again by ',' or text end.
     * @param text the text where the token is taken from the beginning
     * @return the token that exactly matches one Json-element of the list.
     */
    protected String getNextToken(String text) {
        Matcher matcher = xmlUnpairedElementPattern.matcher(text);
        if (matcher.find() && matcher.start() == 0) {
            return matcher.group();
        }
        matcher = xmlPairedElementPattern.matcher(text);
        if (!matcher.find() || matcher.start() != 0) {
            throw new JsonXMLParseException("XML parser: invalid format found!");
        }
        String regexTag = String.format("(?s)<(/?)%s.*?>\\s*", matcher.group(2));
        matcher = Pattern.compile(regexTag).matcher(text);
        int tagsOpened = 0;
        do {
            if (!matcher.find()) {
                throw new JsonXMLParseException("XML parser: invalid format found!");
            }
            tagsOpened += matcher.group(1).isEmpty() ? 1 : -1;
        } while (tagsOpened > 0);
        return text.substring(0, matcher.end());
    }

    protected Pattern getUnpairedPattern() {
        return xmlUnpairedElementPattern;
    }

    protected Pattern getPairedPattern() {
        return xmlPairedElementPattern;
    }
}
