package topics.regexes;

import java.util.Scanner;

/**
 *  The string should consist of two integers separated by the colon. The first integer should
 *  be from 00 to 23 and the second integer should be from 00 to 59.
 * Note that if only one digit in the integer, it should be padded with a leading zero. That is,
 * strings "5:00" and "05:1" don't show time correctly, but "05:00" and "05:01" do.
 * Output "YES" if the given string shows time in the correct format, otherwise output "NO".
 */
public class ClockTimeChecker {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String regex = "([01]\\d|2[0-3]):[0-5]\\d";

        String time = scanner.nextLine();
        System.out.println(time.matches(regex) ? "YES" : "NO");
    }
}
