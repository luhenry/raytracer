
package dev.ludovic.raytracer;

public class Sphere implements Hittable {

    private final Point3 center;
    private final double radius;
    private final Material material;

    public Sphere(Point3 center, double radius, Material material) {
        this.center = center;
        assert radius > 0;
        this.radius = radius;
        this.material = material;
    }

    public boolean hit(Ray ray, double t_min, double t_max, HitRecord[] rec) {
        assert rec.length == 1;

        Vec3 oc = ray.origin().sub(center);
        double a = ray.direction().lengthSquared();
        double half_b = oc.dot(ray.direction());
        double c = oc.lengthSquared() - radius * radius;

        double discriminant = half_b * half_b - a * c;
        if (discriminant < 0) return false;
        double sqrtd = Math.sqrt(discriminant);

        // Find the nearest root that lies in the acceptable range.
        double root = (-half_b - sqrtd) / a;
        if (root < t_min || t_max < root) {
            root = (-half_b + sqrtd) / a;
            if (root < t_min || t_max < root)
                return false;
        }

        Point3 point = ray.apply(root);
        rec[0] = new HitRecord(point, material, root, ray, point.sub(center).div(radius));
        return true;
    }
}