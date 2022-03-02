package topics.matchresults;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a program that extracts and outputs all big numbers from a string. A big number contains
 * at least 10 characters.
 * Output all the big numbers you found. Each number should start with a new line and be
 * followed by its length (the number of digits). Put the colon : character to separate numbers
 * and their lengths.
 */
class BigNumberExtract {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String stringWithNumbers = scanner.nextLine();

        Matcher matcher = Pattern.compile("\\d{10,}").matcher(stringWithNumbers);
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            System.out.println(result.group() + ":" + (result.end() - result.start()));
        }
    }
}
