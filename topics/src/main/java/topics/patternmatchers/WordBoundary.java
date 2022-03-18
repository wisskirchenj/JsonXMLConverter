package topics.patternmatchers;

import java.util.*;
import java.util.regex.*;

/**
 * determine if any of the words of this text start or end with the sequence specified in the
 * first line of the input. If there is, you should output "YES", otherwise output "NO".
 * A word can only contain symbols of the English alphabet. You should ignore the case while
 * searching for matches.
 */
public class WordBoundary {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String part = scanner.nextLine();
        String line = scanner.nextLine();

        Matcher matcher = Pattern.compile(String.format("(?i)\\b%1$s|%1$s\\b", part)).matcher(line);
        System.out.println(matcher.find() ? "YES" : "NO");
    }
}
