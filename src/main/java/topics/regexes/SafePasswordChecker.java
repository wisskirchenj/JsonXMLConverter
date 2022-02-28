package topics.regexes;

import java.util.Scanner;

/**
 * The password is hard to crack if it contains at least one uppercase letter, at least one
 * lowercase letter, at least one digit and includes 12 or more symbols. For a given string
 * you should output "YES" if this password is hard to crack, otherwise output "NO".
 */
class SafePasswordChecker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        boolean safe = password.matches(".{12,}") && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*") && password.matches(".*[0-9].*");
        System.out.println(safe ? "YES" : "NO");
    }
}
