package converter.model;

/**
 * converts an abstract data structure attribute:value into text-lines in XML-format.
 */
public class XMLGenerator {
    private static final String INDENT = "  ";

    /**
     * entry point to start generation of XML-format
     * @return lines of XML-Code
     */
    public String generate(DataStructureElement data) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] xmlLines = data.toXML();
        for (String line : xmlLines) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}


