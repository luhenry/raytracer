
package dev.ludovic.raytracer.hittables;

import dev.ludovic.raytracer.Point3;
import dev.ludovic.raytracer.Ray;
import dev.ludovic.raytracer.Vec3;
import dev.ludovic.raytracer.materials.Material;

public class MovingSphere implements Hittable {

    private final Point3 center0, center1;
    private final double time0, time1;
    private final double radius;
    private final Material material;

    public MovingSphere(Point3 center0, Point3 center1, double time0, double time1, double radius, Material material) {
        this.center0 = center0;
        this.center1 = center1;
        this.time0 = time0;
        this.time1 = time1;
        assert radius > 0;
        this.radius = radius;
        this.material = material;

    }

    public boolean hit(Ray ray, double t_min, double t_max, HitRecord[] rec) {
        assert rec.length == 1;

        Vec3 oc = ray.origin().sub(center(ray.time()));
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
        rec[0] = new HitRecord(point, material, root, ray, point.sub(center(ray.time())).div(radius));
        return true;
    }

    public boolean bounding_box(double time0, double time1, AABB[] output_box) {
        assert output_box.length == 1;

        AABB box0 = new AABB(new Point3(center(time0).sub(radius)), new Point3(center(time0).add(radius)));
        AABB box1 = new AABB(new Point3(center(time1).sub(radius)), new Point3(center(time1).add(radius)));
        output_box[0] = AABB.surrounding_box(box0, box1);
        return true;
    }

    public Point3 center(double time) {
        return new Point3(center1.sub(center0).mul((time - time0) / (time1 - time0)).add(center0));
    }
}