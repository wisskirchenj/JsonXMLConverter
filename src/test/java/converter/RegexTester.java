package converter;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * regex tester main. (based on Oracle Tutorial's RegexTestHarness)
 */
public class RegexTester {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("Enter regexp:");
            Pattern pattern =
                    Pattern.compile(scanner.nextLine());

            System.out.println("Enter text to search in:");
            Matcher matcher =
                    pattern.matcher(scanner.nextLine());

            boolean found = false;
            while (matcher.find()) {
                System.out.printf("I found the text" +
                                " \"%s\" starting at " +
                                "index %d and ending at index %d.%n",
                        matcher.group(),
                        matcher.start(),
                        matcher.end());
                found = true;
            }
            if(!found){
                System.out.println("No match found.");
            }
        }
    }
}
