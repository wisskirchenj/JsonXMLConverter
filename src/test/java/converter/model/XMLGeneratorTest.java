package converter.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class XMLGeneratorTest {

    @Mock
    LeafElement leafElement;

    XMLGenerator xmlGenerator;
    String xmlText = "<attrib>value</attrib>";

    @BeforeEach
    void setUp() {
        xmlGenerator = new XMLGenerator();
    }

    @Test
    void generate() {
        when(leafElement.toXML()).thenReturn(new String[] {xmlText});
        String output = xmlGenerator.generate(leafElement);
        assertEquals(output, xmlText);
    }
}