package topics.matchresults;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  a program searching for passwords in a given text. It is known that:
 * a password consists of digits and/or Latin upper- and lowercase letters in any combination;
 * a password always follows the word "password" (it can be written in upper- and/or lowercase letters)
 * but can be separated from it by any number of whitespaces and colon : characters.
 * Output all passwords found in the text, each password starting with a new line. If the text
 * does not contain any passwords, output "No passwords found." without quotes.
 */
class PasswordFinder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        Matcher matcher = Pattern.compile("password[\\s:]+([a-zA-Z0-9]+)",
                Pattern.CASE_INSENSITIVE).matcher(text);
        boolean found = false;
        while (matcher.find()) {
            found = true;
            System.out.println(matcher.group(1));
        }
        if (!found) {
            System.out.println("No passwords found.");
        }
    }
}
