package converter.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonGeneratorTest {

    @Mock
    LeafElement leafElement;

    JsonGenerator jsonGenerator;
    String jsonText = "\"attribute\": \"value\"";

    @BeforeEach
    void setUp() {
        jsonGenerator = new JsonGenerator();
    }

    @Test
    void generate() {
        when(leafElement.toJson()).thenReturn(new String[] {jsonText});
        String output = jsonGenerator.generate(leafElement);
        assertTrue(output.contains(jsonText));
        assertTrue(output.startsWith("{"));
        assertTrue(output.endsWith("}\n"));
    }
}