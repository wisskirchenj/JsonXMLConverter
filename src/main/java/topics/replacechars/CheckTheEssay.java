package topics.replacechars;

import java.util.Scanner;

/**
 * Your program should make all the following changes (in any context):
 * Franse —> France
 * Eifel tower —> Eiffel Tower
 * 19th —> XIXth
 * 20th —> XXth
 * 21st —> XXIst
 */
class CheckTheEssay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        System.out.println(text.replaceAll("Franse", "France")
                .replaceAll("Eifel t", "Eiffel T")
                .replaceAll("19th", "XIXth").replaceAll("20th", "XXth")
                .replaceAll("21st", "XXIst"));

    }
}
