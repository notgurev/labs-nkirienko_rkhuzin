package se1_prog_lab.shared.util;

import java.util.Arrays;

public class EnumUtils {
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    /**
     * Метод, позволяющий получить из строки либо константу enum, либо null
     *
     * @param enumType тип Enum
     * @param s        строка
     * @param <T>      тип Enum
     * @return null или константу Enum
     */
    public static <T extends Enum<T>> T nullableValueOf(Class<T> enumType, String s) {
        if (s == null || !hasSuchConstant(enumType, s)) {
            return null;
        } else {
            return T.valueOf(enumType, s);
        }
    }

    /**
     * Метод, проверяющий, имеет ли Enum такую константу.
     *
     * @param enumType     тип Enum
     * @param enumConstant строка на проверку
     * @param <T>          тип Enum
     * @return true - если имеет, false если нет.
     */
    public static <T extends Enum<T>> boolean hasSuchConstant(Class<T> enumType, String enumConstant) {
        for (T c : enumType.getEnumConstants()) {
            if (c.name().equals(enumConstant)) {
                return true;
            }
        }
        return false;
    }
}
