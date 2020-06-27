package se1_prog_lab.shared.util;

import java.awt.*;
import java.util.Random;

public class ColorUtils {
    public static Color generateRandomColor() {
        Random r = new Random();
        return new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }
}
