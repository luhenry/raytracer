
package dev.ludovic.raytracer.benchmark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ThreadLocalRandom;

import org.openjdk.jmh.annotations.Benchmark;

import dev.ludovic.raytracer.*;

public class RayTracerBenchmark {

    @Benchmark
    public void render() {
        final PrintWriter out = new PrintWriter(new StringWriter());
        final PrintWriter err = new PrintWriter(System.err, true);

        // Image
        final double aspect_ratio = 16.0 / 9.0;
        final int image_width = 400;
        final int image_height = (int)(image_width / aspect_ratio);
        final int samples_per_pixel = 100;
        final int max_depth = 50;

        // World
        Material material_ground = new Lambertian(new Color(0.8, 0.8, 0.0));
        Material material_center = new Lambertian(new Color(0.1, 0.2, 0.5));
        Material material_left   = new Dielectric(1.5);
        Material material_right  = new Metal(new Color(0.8, 0.6, 0.2), 1.0);

        HittableList world = new HittableList();
        world.add(new Sphere(new Point3( 0.0, -100.5, -1.0), 100.0, material_ground));
        world.add(new Sphere(new Point3( 0.0,    0.0, -1.0),   0.5, material_center));
        world.add(new Sphere(new Point3(-1.0,    0.0, -1.0),   0.5, material_left));
        world.add(new Sphere(new Point3(-1.0,    0.0, -1.0),  -0.4, material_left));
        world.add(new Sphere(new Point3( 1.0,    0.0, -1.0),   0.5, material_right));

        // Camera
        Camera cam = new Camera();

        // Render
        out.printf("P3\n");
        out.printf("%d %d\n", image_width, image_height);
        out.printf("255\n");

        for (int j = image_height-1; j >= 0; --j) {
            for (int i = 0; i < image_width; ++i) {
                Color pixel_color = new Color(0, 0, 0);
                for (int s = 0; s < samples_per_pixel; ++s) {
                double u = ((double)i + ThreadLocalRandom.current().nextDouble(0.0, 1.0)) / (image_width-1);
                double v = ((double)j + ThreadLocalRandom.current().nextDouble(0.0, 1.0)) / (image_height-1);
                pixel_color = new Color(pixel_color.add(ray_color(cam.ray(u, v), world, max_depth)));
                }
                out.printf("%s\n", new Color(pixel_color.div(samples_per_pixel).sqrt()));
            }
        }

        out.flush();
    }

    private Color ray_color(Ray ray, Hittable world, int depth) {
        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (depth <= 0) {
            return new Color(0, 0, 0);
        }

        HitRecord[] rec = new HitRecord[1];
        if (world.hit(ray, 0.001, Double.POSITIVE_INFINITY, rec)) {
            Color[] attenuation = new Color[1];
            Ray[] scattered = new Ray[1];
            if (rec[0].material().scatter(ray, rec[0], attenuation, scattered)) {
                return new Color(attenuation[0].mul(ray_color(scattered[0], world, depth - 1)));
            }
            return new Color(0, 0, 0);
        }

        Vec3 unit_direction = ray.direction().unit();
        double t = 0.5 * (unit_direction.y() + 1.0);
        return new Color(new Color(1.0, 1.0, 1.0).mul(1.0 - t).add(new Color(0.5, 0.7, 1.0).mul(t)));
    }
}
