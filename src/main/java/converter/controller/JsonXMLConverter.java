package converter.controller;


import converter.model.*;
import converter.view.PrinterUI;
import converter.view.ScannerUI;

/**
 * Controller Class of the Json-XML-Converter
 */
public class JsonXMLConverter {

    /**
     * entry point for main application. Converts and prints (one line of) user input.
     */
    public void run() {
        // comment next line in - for check in JetBrains:
        // String userInput = new ScannerUI().getUserInput(ScannerUI.NO_PROMPT).trim();
        String userInput = new ScannerUI()
                .getUserInput("Enter XML or Json to convert (one line):").trim();
        String output;

        try {
            output = convertInput(userInput);
        } catch (JsonXMLParseException e) {
            output = "Json-XML-Parse-Error - Illegal format:" + e.getMessage();
        }
        new PrinterUI(output).print();
    }

    /**
     * Method recognizes format at first non-whitespace character and calls appropriate
     * parser and generator.
     * @param userInput the user input
     * @return the converted output
     */
    String convertInput(String userInput) {
        if (userInput.isEmpty()) {
            throw new JsonXMLParseException("Empty input!");
        }
        switch (userInput.charAt(0)) {
            case '<' -> {
                DataStructureElement dataStructure = new XMLParser(userInput).parse();
                return new JsonGenerator(dataStructure).generate();
            }
            case '{' -> {
                DataStructureElement dataStructure = new JsonParser(userInput).parse();
                return new XMLGenerator(dataStructure).generate();
            }
            default -> throw new JsonXMLParseException("Input is neither valid XML nor Json," +
                    " invalid first non-whitespace character!");
        }
    }
}
