package topics.iterators;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * a method that does the following algorithm:
 *
 * creates List<String> from a given array of strings;
 * using ListIterator, removes all items not starting with "J" ;
 * removes "J" from items beginning with "J" (e.g., JFrame -> Frame);
 * prints all the remaining elements in the reverse order.
 */
public class CutJs {

    public static void processIterator(String[] array) {
        List<String> list = new ArrayList<>(List.of(array));
        // list.removeIf(e -> !e.startsWith("J"));
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if (!element.startsWith("J")) {
                iterator.remove();
            } else {
                iterator.set(element.substring(1));
            }
        }
        while (iterator.hasPrevious()) {
            System.out.println(iterator.previous());
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processIterator(scanner.nextLine().split(" "));
    }
}
