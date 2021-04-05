
package dev.ludovic.raytracer;

import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

public class Color extends Vec3 {

    public Color(double x, double y, double z) {
        super(x, y, z);
    }

    public Color(Vec3 v) {
        super(v.x(), v.y(), v.z());
    }

    @Override
    public String toString() {
        return String.format("%d %d %d", (int)(x() * 255), (int)(y() * 255), (int)(z() * 255));
    }
}
