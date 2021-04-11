
package dev.ludovic.raytracer.hittables;

import dev.ludovic.raytracer.Ray;

public interface Hittable {

    boolean hit(Ray ray, double t_min, double t_max, HitRecord[] rec);

    boolean bounding_box(double time0, double time1, AABB[] output_box);
}