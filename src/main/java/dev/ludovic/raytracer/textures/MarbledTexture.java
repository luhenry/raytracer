
package dev.ludovic.raytracer.textures;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Point3;

public class MarbledTexture implements Texture {

    private final Perlin noise = new Perlin();
    private final double scale;

    public MarbledTexture() {
        this(1.0);
    }

    public MarbledTexture(double scale) {
        this.scale = scale;
    }

    public Color value(double u, double v, Point3 p) {
        return new Color(new Color(1, 1, 1).div(2).mul(1 + Math.sin(scale * p.z() + 10 * noise.turbulence(p))));
    }
}
