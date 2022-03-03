package converter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * for project stage 3 a generic data format has to be generated, which is provided
 * by this Generator-subclass. For every element a hierarchy path has to be printed
 * so we keep track of that with a list field.
 */
public class GenericGenerator extends Generator {

    public GenericGenerator() {
        super();
        // no indentation here
        INDENT = "";
    }

    private final List<String> pathList = new ArrayList<>();

    @Override
    protected String getInitialText() {
        return "";
    }

    @Override
    protected int getInitialIndentLevel() {
        return 0;
    }

    @Override
    protected String getFinalText() {
        return "";
    }

    @Override
    protected void generateEndOfParentAttribute(DataStructureElement data, int indentationLevel) {
        // nothing to do for Generic data format
    }

    @Override
    protected void generateEndOfParent(DataStructureElement data, int indentationLevel) {
        // nothing to do for Generic data format
    }

    @Override
    protected void generateLeafValue(LeafElement data, int indentationLevel) {
        //do nothing - already done in generateAttributes
    }

    /**
     * this is the key method of this generator, that does all the relevant formatted
     * output for an element
     * @param data associated DataStructureElement
     * @param indentationLevel recursion depth for indentation (not used here)
     */
    @Override
    protected void generateAttributes(DataStructureElement data, int indentationLevel) {
        while (pathList.size() > indentationLevel) {
            pathList.remove(pathList.size() - 1);
        }
        pathList.add(data.getAttribute());
        builder.append("Element:\npath = ");
        for (String pathElement : pathList) {
            builder.append(pathElement).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length()).append("\n");
        if (data instanceof LeafElement) {
            generateValue((LeafElement) data);
        }
        if (data.getAttributeElements() != null) {
            generateAttributeElements(data.getAttributeElements());
        }
        builder.append("\n");
    }

    private void generateValue(LeafElement data) {
        builder.append("value = ")
                .append(data.getValue() == null ? "null" : String.format("\"%s\"", data.getValue()))
                .append("\n");
    }

    private void generateAttributeElements(List<LeafElement> elements) {
        builder.append("attributes:\n");
        for (LeafElement element : elements) {
            builder.append(String.format("%s = \"%s\"%n", element.getAttribute(), element.getValue()));
        }
    }
}
