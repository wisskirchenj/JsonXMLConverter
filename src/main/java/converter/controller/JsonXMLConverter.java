package converter.controller;


import converter.model.*;
import converter.view.PrinterUI;
import converter.view.ScannerUI;

/**
 * Controller Class of the Json-XML-Converter
 */
public class JsonXMLConverter {

    // model and view classes as fields - so they can be mocked in unit test :-)
    private ScannerUI scannerUI = new ScannerUI();
    private PrinterUI printerUI = new PrinterUI();
    private JsonGenerator jsonGenerator = new JsonGenerator();
    private XMLGenerator xmlGenerator = new XMLGenerator();
    private GenericGenerator genericGenerator = new GenericGenerator();

    /**
     * entry point for main application. Converts and prints user input.
     * For project stage 2 inout is taken from a text file test.txt - so this is done
     * here too: src/test/resources/data/test.txt
     * @param clArgs the command line arguments
     */
    public void run(String[] clArgs) {

        String userInput = scannerUI.getUserInputFromFile(clArgs[1]).trim();
        //String userInput = scannerUI.getUserInput("Enter XML or Json to convert (one line):").trim();
        String output;

        try {
            output = convertInput(userInput, Integer.parseInt(clArgs[0]));
        } catch (JsonXMLParseException e) {
            output = "Json-XML-Parse-Error - Illegal format:\n" + e.getMessage();
        }
        printerUI.print(output);
    }

    /**
     * Method recognizes format at first non-whitespace character and calls appropriate
     * parser and generator.
     * @param userInput the user input
     * @param mode Output-modus for Generator: 0 = Json<->XML 1 = Generic, 2 = XML, else = Json
     * @return the converted output
     */
    String convertInput(String userInput, int mode) {
        if (userInput.isEmpty()) {
            throw new JsonXMLParseException("Empty input!");
        }
        switch (userInput.charAt(0)) {
            case '<' -> {
                DataStructureElement dataStructure = new XMLParser().parse(userInput);
                return generateOutput(dataStructure, mode, true);
            }
            case '{' -> {
                DataStructureElement dataStructure = new JsonParser().parse(userInput);
                return generateOutput(dataStructure, mode, false);
            }
            default -> throw new JsonXMLParseException("Input is neither valid XML nor Json," +
                    " invalid first non-whitespace character!");
        }
    }

   private String generateOutput(DataStructureElement dataStructure, int mode, boolean parsedXML) {
        switch (mode) {
            case 0 -> {
                return parsedXML ? jsonGenerator.generate(dataStructure) : xmlGenerator.generate(dataStructure);
            }
            case 1 -> {
                return genericGenerator.generate(dataStructure);
            }
            case 2 -> {
                return xmlGenerator.generate(dataStructure);
            }
            default -> {
                return jsonGenerator.generate(dataStructure);
            }
        }
    }
}
