
package dev.ludovic.raytracer.materials;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Ray;
import dev.ludovic.raytracer.Vec3;
import dev.ludovic.raytracer.hittables.HitRecord;

public class Lambertian implements Material {

    private final Color albedo;

    public Lambertian(Color albedo) {
        this.albedo = albedo;
    }

    public boolean scatter(Ray ray, HitRecord rec, Color[] attenuation, Ray[] scattered) {
        assert attenuation.length == 1;
        assert scattered.length == 1;

        Vec3 scatter_direction = rec.normal().add(Vec3.randomUnit());

        // Catch degenerate scatter direction
        if (scatter_direction.nearZero())
            scatter_direction = rec.normal();

        scattered[0] = new Ray(rec.point(), scatter_direction, ray.time());
        attenuation[0] = albedo;
        return true;
    }
}
