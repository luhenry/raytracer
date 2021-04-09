
package dev.ludovic.raytracer.hittables;

import dev.ludovic.raytracer.Point3;
import dev.ludovic.raytracer.Ray;
import dev.ludovic.raytracer.Vec3;
import dev.ludovic.raytracer.materials.Material;

public class Sphere extends MovingSphere {

    public Sphere(Point3 center, double radius, Material material) {
        super(center, center, 0, 1, radius, material);
    }
}