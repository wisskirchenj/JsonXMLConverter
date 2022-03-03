package converter.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XMLParserTest {

    String input;
    DataStructureElement parsedElement;
    JsonXMLParseException exception;

    @Test
    void parseUnpaired() {
        input = "<tag/>";
        assertDoesNotThrow(() -> new XMLParser().parse(input));
        parsedElement = new XMLParser().parse(input);
        assertEquals("tag", parsedElement.getAttribute());
        assertNull(parsedElement.getValue());
        assertNull(parsedElement.getAttributeElements());
    }

    @Test
    void parseInvalidFormat() {
        input = "<tag>sth</teg>";
        exception = assertThrows(JsonXMLParseException.class, () -> new XMLParser().parse(input));
        assertEquals("XML parser: invalid format found!", exception.getMessage());
    }

    @Test
    void parseNestedEasy() {
        input = "<tag><inside>sth</inside></tag>";
        ParentElement parsed = (ParentElement) new XMLParser().parse(input);
        assertEquals("tag", parsed.getAttribute());
        assertNull(parsed.getAttributeElements());
        assertEquals(1, parsed.getValue().size());
        assertEquals("sth", parsed.getValue().get(0).getValue());
        assertEquals("inside", parsed.getValue().get(0).getAttribute());
        assertNull(parsed.getValue().get(0).getAttributeElements());
    }


    @Test
    void parseNestedComplex() {
        input = "<tag a1=\"v1\" >\n<in1 inat1=\"inv1\"    inat2=\"inv2\" inat3=\" \" >sth</in1>\n <a></a><inside \n attrib=\"a\" /> \n </tag>";
        ParentElement parsed = (ParentElement) new XMLParser().parse(input);
        assertEquals("tag", parsed.getAttribute());
        assertEquals(1, parsed.getAttributeElements().size());
        assertEquals("a1",  parsed.getAttributeElements().get(0).getAttribute());
        assertEquals("v1", parsed.getAttributeElements().get(0).getValue());
        assertEquals(3, parsed.getValue().size());
        assertEquals("in1", parsed.getValue().get(0).getAttribute());
        assertEquals("sth", parsed.getValue().get(0).getValue());
        assertEquals(3, parsed.getValue().get(0).getAttributeElements().size());
        assertEquals("inat1",  parsed.getValue().get(0).getAttributeElements().get(0).getAttribute());
        assertEquals("inv2", parsed.getValue().get(0).getAttributeElements().get(1).getValue());
        assertEquals(" ", parsed.getValue().get(0).getAttributeElements().get(2).getValue());
        assertEquals("a", parsed.getValue().get(1).getAttribute());
        assertEquals("", parsed.getValue().get(1).getValue());
        assertEquals("inside", parsed.getValue().get(2).getAttribute());
        assertNull(parsed.getValue().get(2).getValue());
        assertEquals(1, parsed.getValue().get(2).getAttributeElements().size());
        assertEquals("attrib",  parsed.getValue().get(2).getAttributeElements().get(0).getAttribute());
        assertEquals("a", parsed.getValue().get(2).getAttributeElements().get(0).getValue());
    }

    @Test
    void parsePaired() {
        input = "<tag>te\"xt</tag>";
        assertDoesNotThrow(() -> new XMLParser().parse(input));
        parsedElement = new XMLParser().parse(input);
        assertEquals("tag", parsedElement.getAttribute());
        assertEquals("te\"xt", parsedElement.getValue());
        assertNull(parsedElement.getAttributeElements());
    }

    @Test
    void parseEmptyPaired() {
        input = "<tag></tag>";
        assertDoesNotThrow(() -> new XMLParser().parse(input));
        parsedElement = new XMLParser().parse(input);
        assertEquals("tag", parsedElement.getAttribute());
        assertEquals("", parsedElement.getValue());
        assertNull(parsedElement.getAttributeElements());
    }


    @Test
    void parsePairedWithAttributes() {
        input = "<tag att1=\"val1\" att2=\"val2\" > te\"xt </tag>";
        assertDoesNotThrow(() -> new XMLParser().parse(input));
        parsedElement = new XMLParser().parse(input);
        assertEquals("tag", parsedElement.getAttribute());
        assertEquals(" te\"xt ", parsedElement.getValue());
        assertEquals(2, parsedElement.getAttributeElements().size());
        assertEquals("att1",  parsedElement.getAttributeElements()
                .get(0).getAttribute());
        assertEquals("val2", parsedElement.getAttributeElements()
                .get(1).getValue());
    }

    @Test
    void parseEmptyPairedWithAttributes() {
        input = "<tag att1=\"val1\" att2=\"val2\" />";
        assertDoesNotThrow(() -> new XMLParser().parse(input));
        parsedElement = new XMLParser().parse(input);
        assertEquals("tag", parsedElement.getAttribute());
        assertNull(parsedElement.getValue());
        assertEquals(2, parsedElement.getAttributeElements().size());
        assertEquals("att1", parsedElement.getAttributeElements()
                .get(0).getAttribute());
        assertEquals("val2", parsedElement.getAttributeElements()
                .get(1).getValue());
    }
}