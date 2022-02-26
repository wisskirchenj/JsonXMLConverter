package converter.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XMLParserTest {

    String input;
    DataStructureElement leafElement;
    JsonXMLParseException exception;

    @Test
    void parseUnpaired() {
        input = "<tag/>";
        assertDoesNotThrow(() -> new XMLParser(input).parse());
        leafElement = new XMLParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals("", leafElement.getValue());
    }

    @Test
    void parseInvalidFormat() {
        input = "<tag>sth</teg>";
        exception = assertThrows(JsonXMLParseException.class, () -> new XMLParser(input).parse());
        assertEquals("XML parser: invalid XML-format found!", exception.getMessage());
    }

    @Test
    void parseNested() {
        input = "<tag><inside>sth</inside></tag>";
        exception = assertThrows(JsonXMLParseException.class, () -> new XMLParser(input).parse());
        assertEquals("XML parser: no child-elements supported in this version yet!",
                exception.getMessage());
    }

    @Test
    void parsePaired() {
        input = "<tag>te\"xt</tag>";
        assertDoesNotThrow(() -> new XMLParser(input).parse());
        leafElement = new XMLParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals("te\"xt", leafElement.getValue());
    }
}