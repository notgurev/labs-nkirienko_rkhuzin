package se1_prog_lab.relict;

import com.google.inject.Singleton;
import se1_prog_lab.server.ServerApp;

import java.util.logging.Logger;

/**
 * Класс-буфер для "сбора" ответа клиенту.
 */
@Singleton
public class MyResponseBuilder implements ResponseBuilder {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());

    private String responseLine = "";

    /**
     * Добавляет строку к ответу.
     *
     * @param s добавляемая строка.
     */
    @Override
    public void addLineToResponse(String s) {
        logger.finest("Строка \"" + responseLine + "\" добавлена в ответ");
        if (responseLine.equals("")) responseLine = responseLine + s;
        else responseLine = responseLine + '\n' + s;
    }

    /**
     * @return собранный ответ.
     */
    @Override
    public String getResponse() {
        return responseLine;
    }

    /**
     * Очищает буфер ответа.
     */
    @Override
    public void clearResponse() {
        logger.finest("Буфер ответа очищен");
        responseLine = "";
    }
}
