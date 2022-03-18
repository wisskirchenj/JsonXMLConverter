package converter.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {

    String input;
    DataStructureElement dataElement;
    JsonXMLParseException exception;

    @Test
    void parseNull() {
        input = "{ \"tag\": \nnull}";
        dataElement = new JsonParser().parse(input);
        assertEquals("tag", dataElement.getAttribute());
        assertNull(dataElement.getValue());
    }

    @Test
    void parseStringValue() {
        input = "{\n \"tag\" \n: \n\"some text\" \n   \n}";
        dataElement = new JsonParser().parse(input);
        assertEquals("tag", dataElement.getAttribute());
        assertEquals("some text", dataElement.getValue());
    }

    @Test
    void parseInvalidFormat() {
        input = "{name:value}";
        JsonParser jsonParser = new JsonParser();
        exception = assertThrows(JsonXMLParseException.class, () -> jsonParser.parse(input));
        assertTrue(exception.getMessage().startsWith("Json-Parser: Invalid"));
    }

    @Test
    void parseNumberValue() {
        input = "{\n \"tag\" \n: \n23.123 \n   \n}";
        dataElement = new JsonParser().parse(input);
        assertEquals("tag", dataElement.getAttribute());
        assertEquals("23.123", dataElement.getValue());
        input = "{\n \"electron_mass\" \n: \n1.6019\n   \n}";
        dataElement = new JsonParser().parse(input);
        assertEquals("electron_mass", dataElement.getAttribute());
        assertEquals("1.6019", dataElement.getValue());
    }

    @Test
    void parseBooleanValue() {
        input = "{\"tag\":false}";
        dataElement = new JsonParser().parse(input);
        assertEquals("tag", dataElement.getAttribute());
        assertEquals("false", dataElement.getValue());
    }

    @Test
    void parseNested() {
        input = "{\"name\":{\"inner\":12}}";
        dataElement = new JsonParser().parse(input);
        assertEquals("name", dataElement.getAttribute());
        assertEquals(1, ((ParentElement) dataElement).getValue().size());
        assertEquals("inner", ((ParentElement) dataElement).getValue().get(0).getAttribute());
        assertEquals("12", ((ParentElement) dataElement).getValue().get(0).getValue());
    }

    @Test
    void parseInvalidValue() {
        input = "{\"name\":{ \"@att1\":\"val1\", \"#name\":1,}";
        JsonParser jsonParser = new JsonParser();
        exception = assertThrows(JsonXMLParseException.class, () -> jsonParser.parse(input));
        assertTrue(exception.getMessage().startsWith("Json-Parser: Invalid"));
    }

    @Test
    void parseAttributesElement() {
        input = "{\"name\" :{ \"@att1\": \"val1\", \"@att2\" :\"val2\", \"#name\":\"1.71\"}}";
        dataElement = new JsonParser().parse(input);
        assertEquals("name", dataElement.getAttribute());
        assertEquals("1.71", dataElement.getValue());
        assertEquals(2, dataElement.getAttributeElements().size());
        assertEquals("att1",  dataElement
                .getAttributeElements().get(0).getAttribute());
        assertEquals("val2", dataElement
                .getAttributeElements().get(1).getValue());
    }

    @Test
    void parseEmptyAttributesElement() {
        input = "{\"name\" :{ \"@att1\": \"val1\", \"@att2\" :\"val2\", \"#name\": null }}";
        dataElement = new JsonParser().parse(input);
        assertEquals("name", dataElement.getAttribute());
        assertNull(dataElement.getValue());
        assertEquals(2, dataElement.getAttributeElements().size());
        assertEquals("att1", dataElement
                .getAttributeElements().get(0).getAttribute());
        assertEquals("val2", dataElement
                .getAttributeElements().get(1).getValue());
    }
}