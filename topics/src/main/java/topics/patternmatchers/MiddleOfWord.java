package topics.patternmatchers;

import java.util.*;
import java.util.regex.*;

/**
 * determine if any of these words contains this sequence of letters in the middle.
 * The sequence cannot be located at the start or at the end of the word, only in the middle.
 * If there is such a word, you should output "YES", otherwise output "NO". A word can contain
 * only symbols of the English alphabet. You should ignore the case while searching for matches.
 */
public class MiddleOfWord {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String part = scanner.nextLine();
        String line = scanner.nextLine();

        Pattern pattern = Pattern.compile(String.format("(?i)\\B%s\\B", part));
        Matcher matcher = pattern.matcher(line);
        System.out.println(matcher.find() ? "YES" : "NO");
    }
}