
package dev.ludovic.raytracer.textures;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Point3;

public class SolidColor implements Texture {

    private final Color color;

    public SolidColor(Color color) {
        this.color = color;
    }

    public Color value(double u, double v, Point3 p) {
        return color;
    }
}
