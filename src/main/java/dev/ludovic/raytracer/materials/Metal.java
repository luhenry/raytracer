
package dev.ludovic.raytracer.materials;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Ray;
import dev.ludovic.raytracer.Vec3;
import dev.ludovic.raytracer.hittables.HitRecord;

public class Metal implements Material {

    private final Color albedo;
    private final double fuzz;

    public Metal(Color albedo, double fuzz) {
        this.albedo = albedo;
        this.fuzz = Math.min(fuzz, 1);
    }

    public boolean scatter(Ray ray, HitRecord rec, Color[] attenuation, Ray[] scattered) {
        assert attenuation.length == 1;
        assert scattered.length == 1;

        Vec3 reflected = ray.direction().unit().reflect(rec.normal());
        scattered[0] = new Ray(rec.point(), reflected.add(Vec3.randomInUnitSphere().mul(fuzz)), ray.time());
        attenuation[0] = albedo;
        return scattered[0].direction().dot(rec.normal()) > 0;
    }
}
