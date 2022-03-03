package converter.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
        LeafElement leafElement = new LeafElement("attrib", null);
        String xmlText = "<attrib/>\n";
        String output = xmlGenerator.generate(leafElement);
        assertEquals(output, xmlText);
    }

    @Test
    void generateAttributesLeaf() {
        LeafElement element = new LeafElement("tag", "value");
        element.addAttributeElement(new LeafElement("attrib1", "value1"));
        element.addAttributeElement(new LeafElement("attrib2", "value2"));
        String xmlAttributesText = "<tag attrib1=\"value1\" attrib2=\"value2\">value</tag>\n";
        String output = xmlGenerator.generate(element);
        assertEquals(output, xmlAttributesText);
    }


    @Test
    void generateEmptyAttributesLeaf() {
        LeafElement element = new LeafElement("tag",null);
        element.addAttributeElement(new LeafElement("attrib1", "value1"));
        element.addAttributeElement(new LeafElement("attrib2", "value2"));
        String xmlAttributesText = "<tag attrib1=\"value1\" attrib2=\"value2\"/>\n";
        String output = xmlGenerator.generate(element);
        assertEquals(output, xmlAttributesText);
    }
}