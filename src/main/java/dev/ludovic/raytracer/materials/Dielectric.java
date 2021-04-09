
package dev.ludovic.raytracer.materials;

import java.util.concurrent.ThreadLocalRandom;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Ray;
import dev.ludovic.raytracer.Vec3;
import dev.ludovic.raytracer.hittables.HitRecord;

public class Dielectric implements Material {

    private final double ir;

    public Dielectric(double ir) {
        this.ir = ir;
    }

    public boolean scatter(Ray ray, HitRecord rec, Color[] attenuation, Ray[] scattered) {
        double refraction_ratio = rec.front_face() ? (1.0/ir) : ir;

        Vec3 unit_direction = ray.direction().unit();
        double cos_theta = Math.min(unit_direction.neg().dot(rec.normal()), 1.0);
        double sin_theta = Math.sqrt(1.0 - cos_theta * cos_theta);

        boolean cannot_refract = refraction_ratio * sin_theta > 1.0;
        Vec3 direction;
        if (cannot_refract || reflectance(cos_theta, refraction_ratio) > ThreadLocalRandom.current().nextDouble())
            direction = unit_direction.reflect(rec.normal());
        else
            direction = unit_direction.refract(rec.normal(), refraction_ratio);

        attenuation[0] = new Color(1.0, 1.0, 1.0);
        scattered[0] = new Ray(rec.point(), direction, ray.time());
        return true;
    }

    private double reflectance(double cosine, double ref_idx) {
        // Use Schlick's approximation for reflectance.
        double r0 = Math.pow((1 - ref_idx) / (1 + ref_idx), 2);
        return r0 + (1 - r0) * Math.pow(1 - cosine, 5);
    }
}
