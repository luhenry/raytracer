
package dev.ludovic.raytracer;

import java.util.concurrent.ThreadLocalRandom;

public class Dielectric implements Material {

    private final double ir;

    public Dielectric(double ir) {
        this.ir = ir;
    }

    public boolean scatter(Ray r_in, HitRecord rec, Color[] attenuation, Ray[] scattered) {
        double refraction_ratio = rec.front_face() ? (1.0/ir) : ir;

        Vec3 unit_direction = r_in.direction().unit();
        double cos_theta = Math.min(unit_direction.neg().dot(rec.normal()), 1.0);
        double sin_theta = Math.sqrt(1.0 - cos_theta * cos_theta);

        boolean cannot_refract = refraction_ratio * sin_theta > 1.0;
        Vec3 direction;
        if (cannot_refract || reflectance(cos_theta, refraction_ratio) > ThreadLocalRandom.current().nextDouble(0.0, 1.0))
            direction = unit_direction.reflect(rec.normal());
        else
            direction = unit_direction.refract(rec.normal(), refraction_ratio);

        attenuation[0] = new Color(1.0, 1.0, 1.0);
        scattered[0] = new Ray(rec.point(), direction);
        return true;
    }

    private double reflectance(double cosine, double ref_idx) {
        // Use Schlick's approximation for reflectance.
        double r0 = Math.pow((1 - ref_idx) / (1 + ref_idx), 2);
        return r0 + (1 - r0) * Math.pow(1 - cosine, 5);
    }
}
