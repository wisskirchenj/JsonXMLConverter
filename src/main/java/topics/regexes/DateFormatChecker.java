package topics.regexes;

import java.util.*;

/**
 * The input date can be in any of the two formats: yyyy-mm-dd or dd-mm-yyyy.
 * The year must be 19yy or 20yy. - /. symbols can be used as splitters.
 * dd from 01 to 31
 * mm from 01 to 12
 */
class DateFormatChecker {

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        String date = scn.nextLine();
        String dateRegex = "((19|20)\\d{2}[./-](0[1-9]|1[0-2])[./-](0[1-9]|[12]\\d|3[01]))|" +
                "((0[1-9]|[12]\\d|3[01])[./-](0[1-9]|1[0-2])[./-](19|20)\\d{2})";
        System.out.println(date.matches(dateRegex) ? "Yes" : "No");
    }
}
