package se1_prog_lab.server;

import com.google.inject.Singleton;
import se1_prog_lab.server.interfaces.EOTWrapper;

/**
 * Класс-обертка для передачи строк.
 */
@Singleton
public class UtfEOTWrapper implements EOTWrapper {
    /**
     * Символ конца передачи.
     */
    private final char EOT_SYMBOL = '\u0004';

    /**
     * Готовит строку к отправке, добавляя в конец символ конца передачи.
     *
     * @param s строка для отправки.
     * @return обернутая строка.
     */
    @Override
    public String wrap(String s) {
        return s + EOT_SYMBOL;
    }

    /**
     * Убирает символ конца передачи.
     *
     * @param s строка после получения.
     * @return распакованная строка.
     */
    @Override
    public String unwrap(String s) {
        if (hasEOTSymbol(s)) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * Проверяет, есть ли в строке символ конца передачи.
     *
     * @param s строка на проверку.
     * @return true, если есть; false, если нет.
     */
    @Override
    public boolean hasEOTSymbol(String s) {
        return s.length() > 0 && s.charAt(s.length() - 1) == EOT_SYMBOL;
    }
}
