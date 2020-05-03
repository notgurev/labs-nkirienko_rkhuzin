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
     * @param limit     верхний/нижний предел, либо null в случае его отсутствия.
     * @return number, если он не выходит за пределы; null, если выходит за пределы.
     */
    private static Number checkForLimits(String limitType, Number number, Long limit) {
        switch (limitType) {
            case ("MIN"):
                if (number.longValue() > limit) {
                    return number;
                } else {
                    System.out.print("Значение должно быть больше " + limit + ". Попробуйте снова: ");
                }
                break;
            case ("MAX"):
                if (number.longValue() < limit) {
                    return number;
                } else {
                    System.out.print("Значение должно быть меньше " + limit + ". Попробуйте снова: ");
                }
                break;
            case ("NO_LIMIT"):
                return number;
        }
        return number;
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
        System.out.print(messageForConsole);
        float result;
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            try {
                result = Float.parseFloat(input);
                return (float) checkForLimits(limitType, result, limit);
            } catch (NumberFormatException ex) {
                if (canBeNull && input.equals("")) {
                    return null;
                }
                System.out.print("Введенное значение не является числом или превышает его допустимые границы. Попробуйте снова: ");
            }
        }
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
        System.out.print(messageForConsole);
        int result;
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            try {
                result = Integer.parseInt(input);
                return (int) checkForLimits(limitType, result, limit);
            } catch (NumberFormatException ex) {
                if (canBeNull && input.equals("")) {
                    return null;
                }
                System.out.print("Введенное значение не является числом или превышает его допустимые границы. Попробуйте снова: ");
            }
        }
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
        System.out.print(messageForConsole);
        long result;
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            try {
                result = Long.parseLong(input);
                return (long) checkForLimits(limitType, result, limit);
            } catch (NumberFormatException ex) {
                if (canBeNull && input.equals("")) {
                    return null;
                }
                System.out.print("Введенное значение не является числом или превышает его допустимые границы. Попробуйте снова: ");
            }
        }
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
