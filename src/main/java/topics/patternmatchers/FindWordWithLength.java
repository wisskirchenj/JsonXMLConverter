package topics.patternmatchers;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For a given number N and a line with text, output "YES" if this line contains a word
 * with exactly N symbols, otherwise output "NO".
 * A word can contain only symbols of the English alphabet.
 */
public class FindWordWithLength {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = Integer.parseInt(scanner.nextLine());
        String line = scanner.nextLine();

        Matcher matcher = Pattern.compile(String.format("\\b[a-zA-Z]{%d}\\b", size)).matcher(line);
        System.out.println(matcher.find() ? "YES" : "NO");
    }
}
