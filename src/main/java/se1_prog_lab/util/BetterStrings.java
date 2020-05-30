package se1_prog_lab.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс со всякими удобствами для вывода в консоль.
 */
public class BetterStrings {
    /**
     * Метод, создающий строку из массива строк с переносами строк
     *
     * @param s массив строк
     * @return строка с переносами строк
     */
    public static String multiline(Object... s) {
        if (s == null) return "";
        List<Object> input = Arrays.asList(s);
        return input.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining("\n"));
    }

    /**
     * Метод, красящий строку, если она null
     *
     * @param s строка
     * @return покрашенную строку
     */
    public static Object emptyIfNull(Object s) {
        return s == null ? "пусто" : s;
    }
}
