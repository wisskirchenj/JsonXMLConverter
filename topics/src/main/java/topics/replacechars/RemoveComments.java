package topics.replacechars;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Your task is to delete all these ugly useless comments from the code.
 * The programmer wrote "/* this type of comments *\/" as well as "// this type of comments".
 */
public class RemoveComments {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String codeWithComments = scanner.nextLine();

        Matcher matcher = Pattern.compile("/\\*.*?\\*/|//.*$").matcher(codeWithComments);
        System.out.println(matcher.replaceAll(""));
    }
}
