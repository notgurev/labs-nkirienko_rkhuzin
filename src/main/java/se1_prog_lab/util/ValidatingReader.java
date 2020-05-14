package se1_prog_lab.util;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Класс, считывающий и валидирующий Enum, String, Number
 */
public class ValidatingReader {
    /**
     * Метод, проверяющий, имеет ли Enum такую константу.
     *
     * @param enumType     тип Enum
     * @param enumConstant строка на проверку
     * @param <T>          тип Enum
     * @return true - если имеет, false если нет.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static <T extends Enum<T>> boolean hasSuchConstant(Class<T> enumType, String enumConstant) {
        for (T c : enumType.getEnumConstants()) {
            if (c.name().equals(enumConstant)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод, считывающий Enum
     *
     * @param scanner           Scanner
     * @param enumType          тип Enum
     * @param messageForConsole сообщение в консоль
     * @param canBeNull         может ли данный Enum быть null
     * @param <T>               тип Enum
     * @return константу Enum или null
     */
    public static <T extends Enum<T>> T readEnum(Scanner scanner, Class<T> enumType, String messageForConsole, boolean canBeNull) {
        System.out.println(messageForConsole + Arrays.toString(enumType.getEnumConstants()) + ":");
        String input;
        do {
            input = scanner.nextLine().trim();
            if (input.equals("")) {
                if (canBeNull) return null;
                System.out.println("Поле не может быть пустым! Введите непустую строку: ");
            } else if (!hasSuchConstant(enumType, input)) {
                System.out.print("Такого значения данного поля не существует. Попробуйте снова: ");
            } else {
                return Enum.valueOf(enumType, input);
            }
        } while (true);
    }

    /**
     * Проверяет число на выход за пределы.
     *
     * @param limitType тип предела: MIN - минимум, MAX - максимум, NO_LIMIT - отсутствие предела.
     * @param number    проверяемое число.
     * @param limit     верхний/нижний предел.
     * @return true, если не выходит; false, если входит или limit == null.
     */
    private static boolean checkLimits(String limitType, Number number, Long limit) {
        if (limit == null) return false;
        switch (limitType) {
            case ("MIN"):
                return number.longValue() > limit;
            case ("MAX"):
                return number.longValue() < limit;
            default:
                return false;
        }
    }

    /**
     * Выполняет парсинг в Long/Integer/Float
     *
     * @param numberClass X.class, в который нужно парсить.
     * @param input       строка для парсинга.
     * @param <T>         класс-наследник Number.
     * @return число-результат парсинга.
     * @throws NumberFormatException если указан некорректный класс или не удалось сделать парсинг.
     */
    private static <T extends Number> T parseNumber(Class<T> numberClass, String input) throws NumberFormatException {
        if (numberClass == Long.class) {
            return numberClass.cast(Long.parseLong(input));
        } else if (numberClass == Integer.class) {
            return numberClass.cast(Integer.parseInt(input));
        } else if (numberClass == Float.class) {
            return numberClass.cast(Float.parseFloat(input));
        } else throw new NumberFormatException();
    }

    /**
     * Общий класс для считывания чисел из консоли.
     *
     * @param scanner           Scanner, используемый в консоли.
     * @param numberClass       класс-наследник Number, в которое нужно парсить ввод из консоли.
     * @param messageForConsole приглашение на ввод.
     * @param canBeNull         может ли принимать значение null.
     * @param limit             предел.
     * @param limitType         тип предела (MIN/MAX/NO_LIMIT)
     * @param <T>               класс-наследник Number, в которое нужно парсить ввод из консоли.
     * @return число, либо null (если canBeNull == true)
     */
    private static <T extends Number> T readNumber(Scanner scanner, Class<T> numberClass, String messageForConsole, boolean canBeNull, Long limit, String limitType) {
        System.out.print(messageForConsole);
        T result;
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            if (input.equals("")) {
                if (canBeNull) return null;
                System.out.print("Значение не может быть пустым! Попробуйте снова: ");
                continue;
            }
            try {
                result = parseNumber(numberClass, input);
                if (limitType.equals("NO_LIMIT") || checkLimits(limitType, result, limit)) return result;
                else System.out.printf("Значение должно быть меньше %s. Попробуйте снова: ", limit);
            } catch (NullPointerException | NumberFormatException e) {
                System.out.print("Введенное значение не является корректным числом. Попробуйте снова: ");
            }
        }
    }

    /**
     * Считывает Float из консоли с приглашением на ввод и проверками на корректность введенных данных.
     *
     * @param scanner           Scanner, используемый в консоли.
     * @param messageForConsole приглашение на ввод.
     * @param canBeNull         может ли принимать значение null.
     * @param limit             предел.
     * @param limitType         тип предела (MIN/MAX/NO_LIMIT)
     * @return число, либо null (если canBeNull == true)
     */
    public static Float readFloat(Scanner scanner, String messageForConsole, boolean canBeNull, Long limit, String limitType) {
        return readNumber(scanner, Float.class, messageForConsole, canBeNull, limit, limitType);
    }

    /**
     * Считывает Integer из консоли с приглашением на ввод и проверками на корректность введенных данных.
     *
     * @param scanner           Scanner, используемый в консоли.
     * @param messageForConsole приглашение на ввод.
     * @param canBeNull         может ли принимать значение null.
     * @param limit             предел.
     * @param limitType         тип предела (MIN/MAX/NO_LIMIT)
     * @return число, либо null (если canBeNull == true)
     */
    public static Integer readInteger(Scanner scanner, String messageForConsole, boolean canBeNull, Long limit, String limitType) {
        return readNumber(scanner, Integer.class, messageForConsole, canBeNull, limit, limitType);
    }

    /**
     * Считывает Long из консоли с приглашением на ввод и проверками на корректность введенных данных.
     *
     * @param scanner           Scanner, используемый в консоли.
     * @param messageForConsole приглашение на ввод.
     * @param canBeNull         может ли принимать значение null.
     * @param limit             предел.
     * @param limitType         тип предела (MIN/MAX/NO_LIMIT)
     * @return число, либо null (если canBeNull == true)
     */
    public static Long readLong(Scanner scanner, String messageForConsole, boolean canBeNull, Long limit, String limitType) {
        return readNumber(scanner, Long.class, messageForConsole, canBeNull, limit, limitType);
    }

    /**
     * Метод, считывающий String.
     *
     * @param scanner           Scanner, используемый в консоли.
     * @param messageForConsole приглашение на ввод.
     * @param canBeNull         может ли принимать значение null.
     * @param minLength         минимальная длина строки.
     * @return String или null, если canBeNull == true;
     */
    public static String readString(Scanner scanner, String messageForConsole, boolean canBeNull, Integer minLength) {
        System.out.print(messageForConsole);
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("") && !canBeNull) {
                System.out.print("Данное поле не может быть пустым. Попробуйте снова: ");
            } else if (minLength != null && input.length() < minLength) {
                System.out.print("Введенное значение слишком короткое. Попробуйте снова: ");
            } else {
                return input;
            }
        }
    }
}
