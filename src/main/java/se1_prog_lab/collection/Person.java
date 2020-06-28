package se1_prog_lab.collection;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

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
        return "Person{" +
                "location=" + location +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", passportID='" + passportID + '\'' +
                ", hairColor=" + hairColor +
                '}';
    }

    public void setLocation(Integer x, float y, Integer z) {
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

    public void setName(String name) {
        this.name = name;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }
}
