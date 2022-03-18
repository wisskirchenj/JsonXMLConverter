package topics.iteratingoverarrays;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * a program that performs a right rotation on an array by a given number.
 *
 * Input format:
 * The first line is an array of numbers.
 * The second line is the number of rotations.
 *
 * Output format:
 * Resulting array with space-separated elements in a line
 */
public class RightRotator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = Arrays.asList(scanner.nextLine().split("\\s+"));
        Collections.rotate(list, scanner.nextInt());
        list.forEach(e -> System.out.printf("%s ", e));
    }
}