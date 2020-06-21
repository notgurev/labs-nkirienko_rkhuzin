package se1_prog_lab.collection;

import org.hibernate.validator.constraints.Length;
import se1_prog_lab.exceptions.LabWorkFieldException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

import static se1_prog_lab.shared.util.BetterStrings.emptyIfNull;
import static se1_prog_lab.shared.util.BetterStrings.multiline;

/**
 * Класс человека-автора лабораторной работы.
 */
public class Person implements Serializable {
    @NotNull
    private final Location location;
    @NotEmpty
    @NotNull
    private String name;
    @Positive
    private Float height;
    @Length(min = 9)
    @NotNull
    private String passportID;
    private Color hairColor;

    public Person() {
        location = new Location();
    }

    public Person(String name, Float height, String passportID, Color hairColor, Location location) {
        this.name = name;
        this.height = height;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.location = location;
    }

    @Override
    public String toString() {
        return multiline("Имя: " + name,
                "Рост: " + emptyIfNull(height),
                "Номер паспорта: " + passportID,
                "Цвет волос: " + emptyIfNull(hairColor),
                location.toString());
    }

    public void setLocation(Integer x, float y, Integer z) throws LabWorkFieldException {
        if (x == null || z == null) throw new LabWorkFieldException();
        location.setX(x);
        location.setY(y);
        location.setZ(z);
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws LabWorkFieldException {
        if (name == null || name.equals("")) throw new LabWorkFieldException();
        this.name = name;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) throws LabWorkFieldException {
        if (height != null && height <= 0) throw new LabWorkFieldException();
        this.height = height;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) throws LabWorkFieldException {
        if (passportID == null || passportID.equals("") || passportID.length() < 9) throw new LabWorkFieldException();
        this.passportID = passportID;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        // Нет условий
        this.hairColor = hairColor;
    }
}
