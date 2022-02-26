package converter.model;

/**
 * converts an abstract data structure attribute:value into text-lines in Json-format.
 */
public class JsonGenerator {
    private static final String INDENT = "  ";
    private static final String START_LINE = "{\n";
    private static final String END_LINE = "\n}\n";

    DataStructureElement data;

    public JsonGenerator(DataStructureElement dataStructure) {
        data = dataStructure;
    }

    /**
     * entry point to start generation of Json-format
     * @return lines of Json-Code
     */
    public String generate() {
        StringBuilder stringBuilder = new StringBuilder(START_LINE);
        String[] jsonLines = data.toJson();
        for (String line : jsonLines) {
            stringBuilder.append(INDENT).append(line);
            }
        stringBuilder.append(END_LINE);
        return stringBuilder.toString();
    }
}
