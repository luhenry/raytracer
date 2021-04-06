
package dev.ludovic.raytracer.hittables;

import dev.ludovic.raytracer.Ray;

public interface Hittable {

    boolean hit(Ray ray, double t_min, double t_max, HitRecord[] rec);
}