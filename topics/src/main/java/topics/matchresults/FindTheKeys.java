package topics.matchresults;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a program that extracts and outputs all "keys" from a string.
 * The key is a set of characters surrounded by spaces. The key can be of two types:
 * containing digits and/or upper- and lowercase English consonants in any combination;
 * containing special symbols ?!# and/or upper- and lowercase English vowels in any combinations.
 * Note that y is considered to be a consonant in this task.
 *
 * The key always follows the phrase the key is , which can be written in upper- and/or lowercase
 * letters. Each word can be separated by any number of whitespace, including the key-word itself.
 * Output all keys found in the string, each key on a new line.
 * Be careful, the key is always surrounded by spaces or the input string ends with the key
 * (see the second input). For example, the string "The key is d123." won't contain any keys.
 */
class FindTheKeys {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        Matcher matcher = Pattern.compile("(?i)the\\s+key\\s+is\\s+(\\S+)").matcher(text);
        while (matcher.find()) {
            if (matcher.group(1).matches("(?i)[^aeiou_\\W]+")
                    && (matcher.end() == text.length() || text.charAt(matcher.end()) == ' ')) {
                System.out.println(matcher.group(1));
            }
            if (matcher.group(1).matches("(?i)[aeiou_\\W]+")) {
                System.out.println(matcher.group(1));
            }
        }
    }
}
