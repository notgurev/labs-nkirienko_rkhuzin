package se1_prog_lab.collection;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static se1_prog_lab.shared.util.BetterStrings.multiline;

/**
 * Класс координат лаб. работы (?)
 */
public class Coordinates implements Serializable {
    @Max(value = 625)
    private long x;
    @NotNull
    private Float y;

    public Coordinates() {
    }

    public Coordinates(long x, Float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }
}
