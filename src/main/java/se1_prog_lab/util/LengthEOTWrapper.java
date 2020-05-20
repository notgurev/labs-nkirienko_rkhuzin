package se1_prog_lab.util;

import com.google.inject.Singleton;
import se1_prog_lab.exceptions.EOTException;
import se1_prog_lab.util.interfaces.EOTWrapper;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Класс-обертка для передачи строк.
 */
@Singleton
public class LengthEOTWrapper implements EOTWrapper {
    /**
     * Символ конца передачи.
     */
    private final static byte EOT_SYMBOL = (byte) 0xFF;
    private final static int LENGTH_MAX_SIZE = 4;

    /**
     * Готовит строку к отправке, добавляя в конец символ конца передачи.
     *
     * @param s строка для отправки.
     * @return обернутая строка.
     */
    @Override
    public byte[] wrap(byte[] s) {
        byte[] length = ByteBuffer.allocate(LENGTH_MAX_SIZE).putInt(s.length).array();
        byte[] arrayWithEOT = Arrays.copyOf(s, s.length + 1);
        arrayWithEOT[s.length] = EOT_SYMBOL;
        byte[] concatenated = Arrays.copyOf(length, length.length + arrayWithEOT.length);
        System.arraycopy(arrayWithEOT, 0, concatenated, length.length, arrayWithEOT.length);
        return concatenated;
    }

    /**
     * Убирает символ конца передачи.
     *
     * @param s строка после получения.
     * @return распакованная строка.
     */
    @Override
    public byte[] unwrap(byte[] s) throws EOTException {
        if (hasEOTSymbol(s)) {
            int length = ByteBuffer.wrap(Arrays.copyOfRange(s, 0, 4)).getInt();
            return Arrays.copyOfRange(s, LENGTH_MAX_SIZE, LENGTH_MAX_SIZE + length);
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
    public boolean hasEOTSymbol(byte[] s) throws EOTException {
        int length = ByteBuffer.wrap(Arrays.copyOfRange(s, 0, LENGTH_MAX_SIZE)).getInt();
        if (s.length > length + LENGTH_MAX_SIZE + 1) {
            throw new EOTException("The data length does not match the pne specified in the header");
        }
        return s.length == length + LENGTH_MAX_SIZE + 1 && s[length + LENGTH_MAX_SIZE] == EOT_SYMBOL;
    }
}
