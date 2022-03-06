package topics.iterators;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Implement two methods using ListIterator.
 *
 * The method iterateOverList should iterate over the elements from the beginning to the end
 * and add "Hop" after each "Hip".
 * The method printList should print all elements of the list (on a new line).
 */
public class HipHop {

    public static void iterateOverList(ListIterator<String> iter) {
        while (iter.hasNext()) {
            if ("Hip".equals(iter.next())) {
                iter.add("Hop");
            }
        }
    }

    public static void printList(ListIterator<String> iter) {
        iter.forEachRemaining(System.out::println);
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = Arrays.stream(scanner.nextLine().split(" ")).collect(Collectors.toList());
        iterateOverList(list.listIterator());
        printList(list.listIterator());
    }
}