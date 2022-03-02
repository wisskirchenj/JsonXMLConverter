package converter.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLGeneratorTest {

    XMLGenerator xmlGenerator;

    @BeforeEach
    void setUp() {
        xmlGenerator = new XMLGenerator();
    }

    @Test
    void generateLeaf() {
        LeafElement leafElement = new LeafElement("attrib", "value");
        String xmlText = "<attrib>value</attrib>\n";
        String output = xmlGenerator.generate(leafElement);
        assertEquals(output, xmlText);
    }


    @Test
    void generateEmptyLeaf() {
        LeafElement leafElement = new LeafElement("attrib", "");
        String xmlText = "<attrib/>\n";
        String output = xmlGenerator.generate(leafElement);
        assertEquals(output, xmlText);
    }

    @Test
    void generateAttributesLeaf() {
        List<LeafElement> attributeElements = new ArrayList<>();
        attributeElements.add(new LeafElement("attrib1", "value1"));
        attributeElements.add(new LeafElement("attrib2", "value2"));
        LeafAttributesElement element = new LeafAttributesElement("tag",
                attributeElements,"value");
        String xmlAttributesText = "<tag attrib1=\"value1\" attrib2=\"value2\">value</tag>\n";
        String output = xmlGenerator.generate(element);
        assertEquals(output, xmlAttributesText);
    }


    @Test
    void generateEmptyAttributesLeaf() {
        List<LeafElement> attributeElements = new ArrayList<>();
        attributeElements.add(new LeafElement("attrib1", "value1"));
        attributeElements.add(new LeafElement("attrib2", "value2"));
        LeafAttributesElement element = new LeafAttributesElement("tag",
                attributeElements,"");
        String xmlAttributesText = "<tag attrib1=\"value1\" attrib2=\"value2\"/>\n";
        String output = xmlGenerator.generate(element);
        assertEquals(output, xmlAttributesText);
    }
}