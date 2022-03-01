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
        assertEquals("XML parser: unsupported format yet!",
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

    @Test
    void parseEmptyPaired() {
        input = "<tag></tag>";
        assertDoesNotThrow(() -> new XMLParser(input).parse());
        leafElement = new XMLParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals("", leafElement.getValue());
    }


    @Test
    void parsePairedWithAttributes() {
        input = "<tag att1=\"val1\" att2=\"val2\" > te\"xt </tag>";
        assertDoesNotThrow(() -> new XMLParser(input).parse());
        leafElement = new XMLParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals(" te\"xt ", leafElement.getValue());
        assertEquals(2, ((LeafAttributesElement) leafElement).getAttributeElements().size());
        assertEquals("att1", ((LeafAttributesElement) leafElement).getAttributeElements()
                .get(0).getAttribute());
        assertEquals("val2", ((LeafAttributesElement) leafElement).getAttributeElements()
                .get(1).getValue());
    }

    @Test
    void parseEmptyPairedWithAttributes() {
        input = "<tag att1=\"val1\" att2=\"val2\" />";
        assertDoesNotThrow(() -> new XMLParser(input).parse());
        leafElement = new XMLParser(input).parse();
        assertEquals("tag", leafElement.getAttribute());
        assertEquals("", leafElement.getValue());
        assertEquals(2, ((LeafAttributesElement) leafElement).getAttributeElements().size());
        assertEquals("att1", ((LeafAttributesElement) leafElement).getAttributeElements()
                .get(0).getAttribute());
        assertEquals("val2", ((LeafAttributesElement) leafElement).getAttributeElements()
                .get(1).getValue());
    }
}