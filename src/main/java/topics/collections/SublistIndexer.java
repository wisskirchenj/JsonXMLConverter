package topics.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * a program that reads two sequences of numbers and outputs the starting positions of the first and the last
 * occurrences of the second sequence within the first one, or -1 if there is no such occurrence. Numbers must
 * be separated by a space.
 */
public class SublistIndexer {

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            List<Integer> list = Arrays.stream(scanner.nextLine().split("\\s+"))
                    .map(Integer::parseInt).toList();
            List<Integer> subList = Arrays.stream(scanner.nextLine().split("\\s+"))
                    .map(Integer::parseInt).toList();
            System.out.printf("%d %d", Collections.indexOfSubList(list, subList),
                    Collections.lastIndexOfSubList(list, subList));
        }
    }
