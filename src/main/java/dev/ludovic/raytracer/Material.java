
package dev.ludovic.raytracer;

public interface Material {

    boolean scatter(Ray r_in, HitRecord rec, Color[] attenuation, Ray[] scattered);
}
