
package dev.ludovic.raytracer.hittables;

import dev.ludovic.raytracer.Point3;
import dev.ludovic.raytracer.Ray;

public class AABB {

    private final Point3 minimum;
    private final Point3 maximum;

    public AABB(Point3 minimum, Point3 maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Point3 minimum() {
        return minimum;
    }

    public Point3 maximum() {
        return maximum;
    }

    public boolean hit(Ray ray, double t_min, double t_max) {
        for (int a = 0; a < 3; a++) {
            double invD = 1.0 / ray.direction().apply(a);
            double t0 = (minimum.apply(a) - ray.origin().apply(a)) * invD;
            double t1 = (maximum.apply(a) - ray.origin().apply(a)) * invD;
            if (invD < 0.0) {
                double temp  = t0;
                t0 =  t1;
                t1 = temp;
            }
            t_min = Math.max(t0, t_min);
            t_max = Math.min(t1, t_max);
            if (t_max <= t_min)
                return false;
        }
        return true;
    }

    public static AABB surrounding_box(AABB box0, AABB box1) {
        Point3 small = new Point3(
            Math.min(box0.minimum.x(), box1.minimum.x()),
            Math.min(box0.minimum.y(), box1.minimum.y()),
            Math.min(box0.minimum.z(), box1.minimum.z())
        );
        Point3 large = new Point3(
            Math.max(box0.maximum.x(), box1.maximum.x()),
            Math.max(box0.maximum.y(), box1.maximum.y()),
            Math.max(box0.maximum.z(), box1.maximum.z())
        );
        return new AABB(small, large);
    }
}
