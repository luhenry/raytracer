
package dev.ludovic.raytracer.textures;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Point3;

public class NoiseTexture implements Texture {

    private final Perlin noise = new Perlin();
    private final double scale;

    public NoiseTexture() {
        this(1.0);
    }

    public NoiseTexture(double scale) {
        this.scale = scale;
    }

    public Color value(double u, double v, Point3 p) {
        return new Color(new Color(1, 1, 1).mul(noise.noise(new Point3(p.mul(scale)))));
    }
}
