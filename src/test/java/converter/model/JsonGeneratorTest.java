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
        LeafElement leafElement = new LeafElement("attribute", "");
        String jsonText = "\"attribute\": null";
        String output = jsonGenerator.generate(leafElement);
        assertTrue(output.contains(jsonText));
        assertTrue(output.startsWith("{"));
        assertTrue(output.endsWith("}\n"));
    }

    @Test
    void generateAttributesLeaf() {
        List<LeafElement> attributeElements = new ArrayList<>();
        attributeElements.add(new LeafElement("attrib1", "value1"));
        attributeElements.add(new LeafElement("attrib2", "value2"));
        LeafAttributesElement element = new LeafAttributesElement("tag",
                attributeElements,"value");
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
        List<LeafElement> attributeElements = new ArrayList<>();
        attributeElements.add(new LeafElement("attrib1", "value1"));
        attributeElements.add(new LeafElement("attrib2", "value2"));
        LeafAttributesElement element = new LeafAttributesElement("tag",
                attributeElements,"");
        String output = jsonGenerator.generate(element);
        System.out.println(output);
        assertTrue(output.contains("\"tag\": {"));
        assertTrue(output.contains("\"@attrib2\": \"value2\","));
        assertTrue(output.contains("\"#tag\": null"));
        assertTrue(output.startsWith("{"));
        assertTrue(output.endsWith("}\n"));
    }
}