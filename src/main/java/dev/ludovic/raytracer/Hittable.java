
package dev.ludovic.raytracer;

public interface Hittable {

    boolean hit(Ray ray, double t_min, double t_max, HitRecord[] rec);
}