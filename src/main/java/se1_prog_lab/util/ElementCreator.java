package se1_prog_lab.util;

import se1_prog_lab.client.ClientCommandReceiver;
import se1_prog_lab.collection.*;
import se1_prog_lab.exceptions.LabWorkFieldException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

import static se1_prog_lab.util.ValidatingReader.*;

/**
 * Класс, предоставляющий методы для создания экземпляров LabWork
 */
public class ElementCreator {
    /**
     * Метод для создания LW из консоли
     *
     * @param scanner Scanner, откуда считывается информация
     * @return созданный LabWork
     */
    public static LabWork fromConsole(Scanner scanner) {
        String name = readString(scanner, "Введите название: ", false, null);
        long coordinatesX = readLong(scanner, "Введите координату X: ", false, 625L, "MAX");
        Float coordinatesY = readFloat(scanner, "Введите координату Y: ", false, null, "NO_LIMIT");
        Integer minimalPoint = readInteger(scanner, "Введите minimalPoint (можно оставить пустым): ", true, 0L, "MIN");
        String description = readString(scanner, "Введите описание: ", false, null);
        Integer tunedInWorks = readInteger(scanner, "Введите tunedInWorks (можно оставить пустым): ", true, null, "NO_LIMIT");
        Difficulty difficulty = readEnum(scanner, Difficulty.class, "Введите сложность: ", false);
        return new LabWork(name, new Coordinates(coordinatesX, coordinatesY), minimalPoint, description, tunedInWorks, difficulty, createPerson(scanner));
    }

    private static Person createPerson(Scanner scanner) {
        String name = readString(scanner, "Введите имя автора: ", false, null);
        Float height = readFloat(scanner, "Введите рост автора (можно оставить пустым): ", true, 0L, "MIN");
        String passportID = readString(scanner, "Введите номер паспорта: ", false, 9);
        Color hairColor = readEnum(scanner, Color.class, "Введите цвет волос автора (можно оставить пустым): ", true);
        Integer locX = readInteger(scanner, "Введите местоположение по X: ", false, null, "NO_LIMIT");
        float locY = readFloat(scanner, "Введите местоположение по Y: ", false, null, "NO_LIMIT");
        Integer locZ = readInteger(scanner, "Введите местоположение по Z: ", false, null, "NO_LIMIT");
        return new Person(name, height, passportID, hairColor, new Location(locX, locY, locZ));
    }

    /**
     * Метод для создания LW из массива String[]
     *
     * @param args массив полей
     * @return созданный LabWork
     */
    public static LabWork fromStringArray(String[] args) throws LabWorkFieldException, NumberFormatException, ArrayIndexOutOfBoundsException {
        Arrays.setAll(args, i -> args[i].trim());
        IntStream.range(0, args.length).filter(i -> args[i].equals("")).forEach(i -> args[i] = null);
        return new LabWork(
                args[0],
                new Coordinates(Long.parseLong(args[1]), Float.parseFloat(args[2])),
                args[3] == null ? null : Integer.valueOf(args[3]),
                args[4],
                args[5] == null ? null : Integer.valueOf(args[5]),
                Difficulty.valueOf(args[6]),
                new Person(
                        args[7],
                        args[8] == null ? null : Float.parseFloat(args[8]),
                        args[9],
                        nullableValueOf(Color.class, args[10]),
                        new Location(
                                Integer.parseInt(args[11]), Float.parseFloat(args[12]), Integer.valueOf(args[13])
                        )
                )
        );
    }

    public static LabWork createLabWork(LabWorkParams params) {
        LabWork labWork = new LabWork();
        labWork.setName(params.getName());
        labWork.setCoordinates(params.getCoordinateX(), params.getCoordinateY());
        labWork.setMinimalPoint(params.getMinimalPoint());
        labWork.setDescription(params.getDescription());
        labWork.setTunedInWorks(params.getTunedInWorks());
        labWork.setDifficulty(params.getDifficulty());
        labWork.getAuthor().setName(params.getAuthorName());
        labWork.getAuthor().setHeight(params.getAuthorHeight());
        labWork.getAuthor().setPassportID(params.getAuthorPassportID());
        labWork.getAuthor().setHairColor(params.getAuthorHairColor());
        labWork.getAuthor().setLocation(params.getAuthorLocationX(), params.getAuthorLocationY(), params.getAuthorLocationZ());
        labWork.setId(params.getId());
        labWork.setCreationDate(params.getCreationDate());
        return labWork;
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
     * Метод для считывания и создания экземпляра LabWork из скрипта
     *
     * @param currentScanner сканнер, в данный момент использующийся для считывания скрипта из файла
     * @return экземпляр LabWork
     */
    public static LabWork fromScript(Scanner currentScanner) {
        ArrayList<String> dataForLabWorkCreator = new ArrayList<>();
        for (int i = 0; i < LabWork.getNumberOfFields(); i++) {
            dataForLabWorkCreator.add(currentScanner.nextLine());
        }
        String[] inter = new String[dataForLabWorkCreator.size()];
        inter = dataForLabWorkCreator.toArray(inter);
        return ElementCreator.fromStringArray(inter);
    }

    /**
     * Определяет, выполняется ли сейчас скрипт, и вызывает соответствующий конструктор элемента.
     *
     * @param clientCommandReceiver ресивер клиентских комманд.
     * @return созданный элемент.
     */
    public static LabWork buildLabWork(ClientCommandReceiver clientCommandReceiver) {
        if (clientCommandReceiver.getExecutingScripts().isEmpty()) {
            // Создаем LabWork из консоли
            return fromConsole(clientCommandReceiver.getConsoleScanner());
        } else {
            // Создаем LabWork из скрипта
            return fromScript(clientCommandReceiver.getScriptScanner());
        }
    }
}
