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
        dataElement = new JsonParser().parse(input).get(0);
        assertEquals("tag", dataElement.getAttribute());
        assertEquals("", dataElement.getValue());
    }

    @Test
    void parseStringValue() {
        input = "{\n \"tag\" \n: \n\"some text\" \n   \n}";
        dataElement = new JsonParser().parse(input).get(0);
        assertEquals("tag", dataElement.getAttribute());
        assertEquals("some text", dataElement.getValue());
    }

    @Test
    void parseInvalidFormat() {
        input = "{name:value}";
        exception = assertThrows(JsonXMLParseException.class, () -> new JsonParser().parse(input));
        assertEquals("Json parser: invalid Json-format found (array as root element not supported yet)!",
                exception.getMessage());
    }

    @Test
    void parseNumberValue() {
        input = "{\n \"tag\" \n: \n23.123 \n   \n}";
        dataElement = new JsonParser().parse(input).get(0);
        assertEquals("tag", dataElement.getAttribute());
        assertEquals("23.123", dataElement.getValue());
        input = "{\n \"electron_mass\" \n: \n1.6019e-34\n   \n}";
        dataElement = new JsonParser().parse(input).get(0);
        assertEquals("electron_mass", dataElement.getAttribute());
        assertEquals("1.6019e-34", dataElement.getValue());
    }

    @Test
    void parseBooleanValue() {
        input = "{\"tag\":false}";
        dataElement = new JsonParser().parse(input).get(0);
        assertEquals("tag", dataElement.getAttribute());
        assertEquals("false", dataElement.getValue());
    }

    @Test
    void parseNested() {
        input = "{\"name\":{\"inner\":12}}";
        exception = assertThrows(JsonXMLParseException.class, () -> new JsonParser().parse(input));
        assertEquals("Json parser: unsupported format yet!",
                exception.getMessage());
    }

    @Test
    void parseInvalidValue() {
        input = "{\"name\":{ \"@att1\":\"val1\", \"#name\":1,}";
        exception = assertThrows(JsonXMLParseException.class, () -> new JsonParser().parse(input));
        assertEquals("Json parser: unsupported format yet!", exception.getMessage());
    }

    @Test
    void parseAttributesElement() {
        input = "{\"name\" :{ \"@att1\": \"val1\", \"@att2\" :\"val2\", \"#name\":\"1.71\"}}";
        dataElement = new JsonParser().parse(input).get(0);
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
        dataElement = new JsonParser().parse(input).get(0);
        assertEquals("name", dataElement.getAttribute());
        assertEquals("", dataElement.getValue());
        assertEquals(2, dataElement.getAttributeElements().size());
        assertEquals("att1", dataElement
                .getAttributeElements().get(0).getAttribute());
        assertEquals("val2", dataElement
                .getAttributeElements().get(1).getValue());
    }
}