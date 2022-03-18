package topics.genericmethods;

import java.util.Arrays;
import java.util.Objects;

public class ArrayUtils {

    public static <T> T getFirst(T[] array) {
        return array.length == 0 ? null : array[0];
    }

    public static <E> boolean hasNull(E[] array) {
        return Arrays.stream(array).anyMatch(Objects::isNull);
    }

    public static <T> String info(T[] array) {
        return Arrays.toString(array);
    }
}
