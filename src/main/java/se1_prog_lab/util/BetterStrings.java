package se1_prog_lab.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс со всякими удобствами для вывода в консоль.
 */
public class BetterStrings {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

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
     * Метод, красящий строку в указанный цвет
     *
     * @param s     строка
     * @param color цвет
     * @return покрашенную строку
     */
    public static String colored(String s, String color) {
        return color + s + RESET;
    }

    /**
     * Метод, красящий строку, если она null
     *
     * @param s строка
     * @return покрашенную строку
     */
    public static Object blueIfNull(Object s) {
        return s == null ? colored("пусто", BLUE) : s;
    }

    public static String red(String s) {
        return colored(s, RED);
    }

    public static String yellow(String s) {
        return colored(s, YELLOW);
    }
}
