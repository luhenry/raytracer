
package dev.ludovic.raytracer.materials;

import dev.ludovic.raytracer.Color;
import dev.ludovic.raytracer.Ray;
import dev.ludovic.raytracer.hittables.HitRecord;

public interface Material {

    boolean scatter(Ray r_in, HitRecord rec, Color[] attenuation, Ray[] scattered);
}
