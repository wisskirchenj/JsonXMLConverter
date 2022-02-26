package converter.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafElementTest {

    LeafElement nullElement;
    String nullAttribute = "attribute";
    LeafElement element;
    String attribute = "Movie";
    String value = "\"Nasty\" string!";

    @BeforeEach
    void setUp() {
        nullElement = new LeafElement(nullAttribute, "");
        element = new LeafElement(attribute, value);
    }

    @Test
    void toXML() {
        String expected = "<" + attribute + ">" + value + "</" + attribute +">";
        assertEquals(1, element.toXML().length);
        assertEquals(expected, element.toXML()[0]);
        expected = "<" + nullAttribute + "/>";
        assertEquals(1, nullElement.toXML().length);
        assertEquals(expected, nullElement.toXML()[0]);
    }

    @Test
    void toJson() {
        String expected = "\"Movie\": \"\\\"Nasty\\\" string!\"";
        assertEquals(1, element.toJson().length);
        assertEquals(expected, element.toJson()[0]);
        expected = "\"attribute\": null";
        assertEquals(1, nullElement.toJson().length);
        assertEquals(expected, nullElement.toJson()[0]);
    }
}