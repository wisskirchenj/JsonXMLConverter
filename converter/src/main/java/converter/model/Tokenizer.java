package converter.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Abstract base class of Helper classes that implement the tokenization of data structure
 * elements during parsing of Json- or XML-input.
 * Upon calling tokenizeInput(String text) the tokens list is filled with the result of this run.
 * The tokenizes offers this result as Queue or List.
 * However, besides also the tokens list can be "manipulated" from outside by the list reference
 * or the add-method.
 */
public abstract class Tokenizer {

    private final LinkedList<String> tokens = new LinkedList<>();

    /**
     * return the result of the tokenizer as a Queue-object
     * @return the content of the tokenizer as Queue
     */
    public Queue<String> asQueue() {
        return tokens;
    }

    /**
     * return the result of the tokenizer as a List-object
     * @return the content of the tokenizer as List
     */
    public List<String> asList() {
        return tokens;
    }

    /**
     * convenience method to see if the tokenizer queue/list is empty
     * @return true if queue is empty, false otherwise
     */
    public boolean hasNoTokens() {
        return tokens.isEmpty();
    }

    /**
     * convenience method to add a new token to the end of the list or queue. This manipulation of
     * the tokens list is needed in the Json-Parser to implement the complex attributes logic.
     * @param token the token to be added
     * @return the result of the List's add
     */
    public boolean addToken(String token) {
        return tokens.add(token);
    }

    /**
     * entry point for a rather complicated parsing of a string, that comprises all data of an
     * arbitrary Json-object (even the root-element in first call) into a list of tokens, that
     * precisely contain the data of one child-element. It has a sub call to a method matching one next token.
     */
    protected void tokenizeInput(String text) {
        int position = 0;
        String token;
        do {
            token = getNextToken(text.substring(position));
            position += token.length() + 1;
            token = token.trim();
            if (token.matches("(?s)[^,{]*?(\\[).*")) {
                // hook needed for Json - not relevant for XML...
                token = token.substring(0, token.indexOf('['))
                        + modifyArrayToken(token.substring(token.indexOf('[')));
            }
            tokens.offer(token);
        } while (position < text.length());
    }

    /**
     * this method-hook gives the JsonTokenizer the opportunity to modify this token by inserting "element":
     * keys before each value, so that the subsequent parsing of the contents can be performed as if there
     * were a standard Json child element's list
     */
    protected String modifyArrayToken(String token) {
        // default implementation does nothing - is overridden by JsonTokenizer
        return token;
    }

    /**
     * parser-dependent logic to get the next token form the beginning of the given text.
     * Method has to be implemented in derived class.
     * @param text text to search for a token
     * @return the token found
     */
    protected abstract String getNextToken(String text);
}
