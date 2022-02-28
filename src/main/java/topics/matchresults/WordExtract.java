package topics.matchresults;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a program extracting all words that include the substring "program" from a given text.
 * The text can be large enough. You should not count whitespaces, punctuation marks and other
 * special characters as parts of extracted words. Search should be case-insensitive
 */
class WordExtract {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        Matcher matcher = Pattern.compile("(?i)\\w*program\\w*").matcher(text);
        while (matcher.find()) {
            System.out.printf("%d %s%n", matcher.start(), matcher.group());
        }
    }
}
