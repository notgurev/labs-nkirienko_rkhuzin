package se1_prog_lab.shared.util;

import se1_prog_lab.collection.*;

import java.time.LocalDateTime;

/**
 * Класс, предоставляющий методы для создания экземпляров LabWork
 */
public class ElementCreator {
    public static LabWork createLabWork(LabWorkParams p) {
        LabWork lw = new LabWork(
                p.getName(),
                new Coordinates(p.getCoordinateX(), p.getCoordinateY()),
                p.getMinimalPoint(),
                p.getDescription(),
                p.getTunedInWorks(),
                p.getDifficulty(),
                new Person(
                        p.getAuthorName(),
                        p.getAuthorHeight(),
                        p.getAuthorPassportID(),
                        p.getAuthorHairColor(),
                        new Location(
                                p.getAuthorLocationX(),
                                p.getAuthorLocationY(),
                                p.getAuthorLocationZ())
                )
        );
        lw.setId(p.getId());
        LocalDateTime creationDate = p.getCreationDate();
        lw.setCreationDate(creationDate == null ? LocalDateTime.now() : creationDate);
        return lw;
    }
}
