package topics.replacechars;

import java.util.Scanner;

/**
 * For a given string you should remove all HTML tags from it. An HTML tag starts with
 * the symbol "<" and ends with the symbol ">".
 */
public class RemoveHTMLTags {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String stringWithHtmlTags = scanner.nextLine();

        System.out.println(stringWithHtmlTags.replaceAll("<[^>]+>", ""));
    }
}
