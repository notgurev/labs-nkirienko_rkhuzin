package se1_prog_lab.shared.util;

import se1_prog_lab.collection.*;

import java.util.Arrays;
import java.util.stream.IntStream;

import static se1_prog_lab.shared.util.ValidatingReader.hasSuchConstant;

/**
 * Класс, предоставляющий методы для создания экземпляров LabWork
 */
public class ElementCreator {
    /**
     * Метод для создания LW из массива String[]
     *
     * @param args массив полей
     * @return созданный LabWork
     */
    public static LabWork fromStringArray(String[] args) throws NumberFormatException, ArrayIndexOutOfBoundsException {
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
}
