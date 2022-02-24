package converter.model;

/**
 * parse errors in any depth on the calling stack during parsing
 * cause exception object of this class to be thrown to the Controller.
 */
public class JsonXMLParseException extends RuntimeException {

    public JsonXMLParseException(String message) {
        super(message);
    }
}
