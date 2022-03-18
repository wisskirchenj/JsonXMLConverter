package converter.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void generateArray() {
        ParentElement parent = new ParentElement();
        parent.setAttribute("parent");
        parent.addAttributeElement(new LeafElement("parentattrib", "value"));
        parent.addValueElement(new LeafElement("data", "first"));
        ParentElement child = new ParentElement();
        child.setAttribute("data");
        child.addValueElement(new LeafElement("grandchild1", "Peter"));
        child.addValueElement(new LeafElement("grandchild2", "John"));
        parent.addValueElement(child);
        LeafElement leafElement = new LeafElement("data", null);
        leafElement.addAttributeElement(new LeafElement("someatt", ""));
        parent.addValueElement(leafElement);
        String output = jsonGenerator.generate(parent);
        System.out.println(output);
        assertTrue(output.contains("\"#parent\": ["));
        assertTrue(output.contains("\"@someatt\": \"\","));
        assertTrue(output.contains("grandchild2\": \"John"));
    }
}