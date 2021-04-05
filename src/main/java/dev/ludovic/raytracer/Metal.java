
package dev.ludovic.raytracer;

public class Metal implements Material {

    private final Color albedo;
    private final double fuzz;

    public Metal(Color albedo, double fuzz) {
        this.albedo = albedo;
        this.fuzz = Math.min(fuzz, 1);
    }

    public boolean scatter(Ray r_in, HitRecord rec, Color[] attenuation, Ray[] scattered) {
        assert attenuation.length == 1;
        assert scattered.length == 1;

        Vec3 reflected = r_in.direction().unit().reflect(rec.normal());
        scattered[0] = new Ray(rec.point(), reflected.add(Vec3.randomInUnitSphere().mul(fuzz)));
        attenuation[0] = albedo;
        return scattered[0].direction().dot(rec.normal()) > 0;
    }
}
