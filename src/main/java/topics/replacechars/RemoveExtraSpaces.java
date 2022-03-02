package topics.replacechars;

import java.util.Scanner;

/**
 * a program that reads a text and removes all extra spaces. The program must replace all repeating
 * spaces and tabulations between words with a single space character (' ').
 */
class RemoveExtraSpaces {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        System.out.println(text.replaceAll("\\s+", " "));
    }
}
