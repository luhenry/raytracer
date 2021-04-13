
package dev.ludovic.raytracer.textures;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Point3;

public class CheckerTexture implements Texture {

    private final Texture even;
    private final Texture odd;

    public CheckerTexture(Color even, Color odd) {
        this.even = new SolidColor(even);
        this.odd = new SolidColor(odd);
    }

    public CheckerTexture(Texture even, Texture odd) {
        this.even = even;
        this.odd = odd;
    }

    public Color value(double u, double v, Point3 p) {
        double sines = Math.sin(10 * p.x()) * Math.sin(10 * p.y()) * Math.sin(10 * p.z());
        return sines < 0 ? odd.value(u, v, p) : even.value(u, v, p);
    }
}
