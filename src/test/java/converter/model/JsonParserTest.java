package converter.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {

    String input;
    DataStructureElement leafElement;
    JsonXMLParseException exception;

    @Test
    void parseNull() {
        input = "{ \"tag\": \nnull}";
        leafElement = new JsonParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals("", leafElement.getValue());
    }

    @Test
    void parseStringValue() {
        input = "{\n \"tag\" \n: \n\"some text\" \n   \n}";
        leafElement = new JsonParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals("some text", leafElement.getValue());
    }

    @Test
    void parseInvalidFormat() {
        input = "{name:value}";
        exception = assertThrows(JsonXMLParseException.class, () -> new JsonParser(input).parse());
        assertEquals("Json parser: invalid Json-format found (array as root element not supported yet)!",
                exception.getMessage());
    }

    @Test
    void parseNumberValue() {
        input = "{\n \"tag\" \n: \n23.123 \n   \n}";
        leafElement = new JsonParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals("23.123", leafElement.getValue());
        input = "{\n \"electron_mass\" \n: \n1.6019e-34\n   \n}";
        leafElement = new JsonParser(input).parse();
        assertEquals("electron_mass", leafElement.getAttribute());
        assertEquals("1.6019e-34", leafElement.getValue());
    }

    @Test
    void parseBooleanValue() {
        input = "{\"tag\":false}";
        leafElement = new JsonParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals("false", leafElement.getValue());
    }

    @Test
    void parseNested() {
        input = "{\"name\":{\"inner\":12}}";
        exception = assertThrows(JsonXMLParseException.class, () -> new JsonParser(input).parse());
        assertEquals("Json parser: no child-objects supported in this version yet!",
                exception.getMessage());
    }

    @Test
    void parseInvalidValue() {
        input = "{\"name\":value}";
        exception = assertThrows(JsonXMLParseException.class, () -> new JsonParser(input).parse());
        assertEquals("Json parser: invalid Json-format found!", exception.getMessage());
    }

}