package converter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * for project stage 3 a generic data format has to be generated, which is provided
 * by this Generator-subclass. For every element a hierarchy path has to be printed,
 * so we keep track of that with a list field and the recursionLevel.
 */
public class GenericGenerator extends Generator {

    private int recursionLevel = 0;
    private final List<String> pathList = new ArrayList<>();


    /**
     * The specification of GenericData format is without indentation. This method
     * achieves this by resetting the recursion increment indentationLevel.
     * However, for the path generation the Generator must keep track on the recursionLevel,
     * which is therefore incremented here (and decremented later on recursion exits).
     * @return -1 to prevent any indentation in child-elements.
     */
    @Override
    protected int getExtraIndent(DataStructureElement data) {
        recursionLevel++;
        return -1;
    }

    /**
     * as this is one of two exit hooks out of the recursion in Generator,
     * we must decrement our bookkeeping in recursion level.
     * Nothing else to format.
     * @param data associated DataStructureElement -> not user here
     * @param indentationLevel recursion depth for indentation -> not used here
     */
    @Override
    protected void generateEndOfParent(ParentElement data, int indentationLevel) {
        recursionLevel--;
    }

    /**
     * as this is one of two exit hooks out of the recursion in Generator,
     * we must decrement our bookkeeping in recursion level.
     * Nothing else to format. Leaf's Value is already appended in getAttributes in this special
     * generator.
     * @param data associated DataStructureElement -> not user here
     * @param indentationLevel recursion depth for indentation -> not used here
     */
    @Override
    protected void generateLeafValue(LeafElement data, int indentationLevel) {
        recursionLevel--;
    }

    /**
     * this is the key method of this generator, that does all the relevant formatted
     * output for an element
     * @param data associated DataStructureElement
     * @param indentationLevel recursion depth for indentation (not used here)
     */
    @Override
    protected void generateAttributes(DataStructureElement data, int indentationLevel) {
        while (pathList.size() > recursionLevel) {
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
