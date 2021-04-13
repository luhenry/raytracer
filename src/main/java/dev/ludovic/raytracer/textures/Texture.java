
package dev.ludovic.raytracer.textures;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Point3;

public interface Texture {

    Color value(double u, double v, Point3 p);
}
