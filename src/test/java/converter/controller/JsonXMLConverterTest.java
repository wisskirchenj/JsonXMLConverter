package converter.controller;

import converter.model.*;
import converter.view.PrinterUI;
import converter.view.ScannerUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonXMLConverterTest {

    @Mock
    ScannerUI scannerUI;
    @Mock
    PrinterUI printerUI;
    @Mock
    XMLGenerator xmlGenerator;

    @InjectMocks
    JsonXMLConverter converter;

    @Captor
    private ArgumentCaptor<String> captor;

    JsonXMLParseException exception;

    @BeforeEach
    void setUp() {
    }

    @Test
    void runInvalid() {
        when(scannerUI.getUserInputFromFile()).then((Answer<String>) invocation -> "something invalid");
        converter.run();
        String errorMessageStart = "Json-XML-Parse-Error - Illegal format:";
        verify(printerUI).print(captor.capture());
        assertTrue(captor.getValue().startsWith(errorMessageStart));
    }


    @Test
    void runValid() {
        when(scannerUI.getUserInputFromFile())
                .then((Answer<String>) invocation -> "{\"tag\":1}");
        when(xmlGenerator.generate(ArgumentMatchers.any())).thenReturn("<tag>1</tag>");
        converter.run();
        verify(printerUI).print(captor.capture());
        assertTrue(captor.getValue().startsWith("<tag>"));
    }

    @Test
    void convertInputInvalid() {
        String invalidInput = "something invalid";
        exception = assertThrows(JsonXMLParseException.class, () -> converter.convertInput(invalidInput));
        assertEquals("Input is neither valid XML nor Json," +
                " invalid first non-whitespace character!", exception.getMessage());
    }

    @Test
    void convertInputEmpty() {
        exception = assertThrows(JsonXMLParseException.class, () -> converter.convertInput(""));
        assertEquals("Empty input!", exception.getMessage());
    }

    @Test
    void convertInputValidJson() {
        String validJsonInput = "{\"a\":1}";
        converter.convertInput(validJsonInput);
        verify(xmlGenerator, times(1)).generate(any(DataStructureElement.class));
    }

    @Test
    void convertInputValidXML() {
        String validXMLInput = "<tag/>";
        converter.convertInput(validXMLInput);
        verify(xmlGenerator, times(1)).generate(any(DataStructureElement.class));
    }
}