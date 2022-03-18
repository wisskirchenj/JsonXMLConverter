package topics.genericmethods;

import java.util.List;

public class ListMultiplicator {

    /**
     Repeats original list content provided number of times
     @param list list to repeat
     @param n times to repeat, should be zero or greater
     */
    public static void multiply(List<?> list, int n) {
        multiplyTyped(list, n);
    }

    public static <T> void multiplyTyped(List<T> list, int n) {
        if (n == 0) {
            list.clear();
            return;
        }
        if (n > 1) {
            List<T> listCopy = List.copyOf(list);
            for (int i = 0; i < n - 1; i++) {
                list.addAll(listCopy);
            }
        }
    }
}
