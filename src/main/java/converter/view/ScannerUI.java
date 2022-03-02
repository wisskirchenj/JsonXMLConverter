package converter.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

/**
 * simple UI-class, that prompts the User for Input and returns the
 * scan result to the caller.
 */
public class ScannerUI {

    public static final String NO_PROMPT = null;

    private final Scanner scanner = new Scanner(System.in);

    /**
     * prompts for an input (currently one line as string)
     * @param promptText the promptText
     * @return line of user input
     */
    public String getUserInput(String promptText) {
        if (!Objects.equals(promptText, NO_PROMPT)) {
            System.out.println(promptText);
        }
        return scanner.nextLine();
    }

    public String getUserInputFromFile(){
        try {
            return new String(Files.readAllBytes(
                    Paths.get("src/test/resources/data/test.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
