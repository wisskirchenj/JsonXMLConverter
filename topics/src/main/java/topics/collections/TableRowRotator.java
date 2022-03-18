package topics.collections;

import java.util.*;

/**
 * You have a table of integer numbers. You should rotate rows of the table by the specified distance.
 *
 * Input data format
 * The first line contains two numbers: a number of rows and a number of columns of the table.
 * The following lines describe rows of the table. In each row, all elements are separated by spaces.
 * The last line consists of one number, which is the distance for rotating.
 *
 * Output data format
 * Output the resulting table. Separate numbers by a single space in the output.
 */
class TableRowRotator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] dimensions = Arrays.stream(scanner.nextLine().split("\\s+"))
                .mapToInt(Integer::parseInt).toArray();
        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < dimensions[0]; i++) {
            rows.add(scanner.nextLine().split("\\s+"));
        }
        Collections.rotate(rows, scanner.nextInt());
        rows.forEach(row -> {
            for (String entry: row) {
                System.out.printf("%s ", entry);
            }
            System.out.println();
        });
    }
}
