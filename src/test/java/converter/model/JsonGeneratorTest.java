package converter.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonGeneratorTest {

    JsonGenerator jsonGenerator;

    @BeforeEach
    void setUp() {
        jsonGenerator = new JsonGenerator();
    }

    @Test
    void generateLeaf() {
        LeafElement leafElement = new LeafElement("attribute", "value");
        String jsonText = "\"attribute\": \"value\"";
        String output = jsonGenerator.generate(leafElement);
        assertTrue(output.contains(jsonText));
        assertTrue(output.startsWith("{"));
        assertTrue(output.endsWith("}\n"));
    }


    @Test
    void generateEmptyLeaf() {
        LeafElement leafElement = new LeafElement("attribute", null);
        String jsonText = "\"attribute\": null";
        String output = jsonGenerator.generate(leafElement);
        assertTrue(output.contains(jsonText));
        assertTrue(output.startsWith("{"));
        assertTrue(output.endsWith("}\n"));
    }

    @Test
    void generateAttributesLeaf() {
        LeafElement element = new LeafElement("tag", "value");
        element.addAttributeElement(new LeafElement("attrib1", "value1"));
        element.addAttributeElement(new LeafElement("attrib2", "value2"));
        String output = jsonGenerator.generate(element);
        System.out.println(output);
        assertTrue(output.contains("\"tag\": {"));
        assertTrue(output.contains("\"@attrib2\": \"value2\","));
        assertTrue(output.contains("\"#tag\": \"value\""));
        assertTrue(output.startsWith("{"));
        assertTrue(output.endsWith("}\n"));
    }


    @Test
    void generateEmptyAttributesLeaf() {
        LeafElement element = new LeafElement("tag",null);
        element.addAttributeElement(new LeafElement("attrib1", "value1"));
        element.addAttributeElement(new LeafElement("attrib2", "value2"));
        String output = jsonGenerator.generate(element);
        System.out.println(output);
        assertTrue(output.contains("\"tag\": {"));
        assertTrue(output.contains("\"@attrib2\": \"value2\","));
        assertTrue(output.contains("\"#tag\": null"));
        assertTrue(output.startsWith("{"));
        assertTrue(output.endsWith("}\n"));
    }
}