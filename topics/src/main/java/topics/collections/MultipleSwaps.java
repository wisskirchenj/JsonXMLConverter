package topics.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * a program to sequentially swap elements by their indexes in a given list. Indexes of the elements start with 0
 * and are always less than the size of the list.
 * Input data format
 * The first line contains elements of the list. The second line contains the number of swaps.
 * Then follow the lines with descriptions of the swaps. Each line contains two numbers: indexes of swapped elements.
 * Output data format
 * All elements of the modified list separated by spaces must be output in one line.
 */
public class MultipleSwaps {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> list = Stream.of(scanner.nextLine().split("\\s+"))
                .map(Integer::parseInt).collect(Collectors.toList());
        int numberOfSwaps = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numberOfSwaps; i++) {
            int[] indexes = Arrays.stream(scanner.nextLine().split("\\s+"))
                    .mapToInt(Integer::parseInt).toArray();
            Collections.swap(list, indexes[0], indexes[1]);
        }
        list.forEach((e)->System.out.printf("%d ", e));
    }
}

