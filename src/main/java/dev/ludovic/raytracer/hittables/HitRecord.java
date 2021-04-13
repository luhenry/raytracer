
package dev.ludovic.raytracer.hittables;

import dev.ludovic.raytracer.Point3;
import dev.ludovic.raytracer.Ray;
import dev.ludovic.raytracer.Vec3;
import dev.ludovic.raytracer.materials.Material;

public class HitRecord {

    private final Point3 point;
    private final Vec3 normal;
    private final Material material;
    private final double t;
    private final boolean front_face;
    private final double u, v;

    public HitRecord(Point3 point, Vec3 normal, Material material, double t, boolean front_face, double u, double v) {
        this.point = point;
        this.normal = normal;
        this.material = material;
        this.t = t;
        this.front_face = front_face;
        this.u = u;
        this.v = v;
    }

    public HitRecord(Point3 point, Material material, double t, Ray ray, Vec3 outward_normal, double u, double v) {
        this.point = point;
        this.material = material;
        this.t = t;
        this.front_face = ray.direction().dot(outward_normal) < 0;
        this.normal = this.front_face ? outward_normal : outward_normal.neg();
        this.u = u;
        this.v = v;
    }

    public Point3 point() {
        return point;
    }

    public Vec3 normal() {
        return normal;
    }

    public Material material() {
        return material;
    }

    public double t() {
        return t;
    }

    public boolean front_face() {
        return front_face;
    }

    public double u() {
        return u;
    }

    public double v() {
        return v;
    }
}
