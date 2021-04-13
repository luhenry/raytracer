
package dev.ludovic.raytracer;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.ludovic.raytracer.hittables.BVHNode;
import dev.ludovic.raytracer.hittables.HitRecord;
import dev.ludovic.raytracer.hittables.HittableList;
import dev.ludovic.raytracer.hittables.MovingSphere;
import dev.ludovic.raytracer.hittables.Sphere;
import dev.ludovic.raytracer.materials.Dielectric;
import dev.ludovic.raytracer.materials.Lambertian;
import dev.ludovic.raytracer.materials.Material;
import dev.ludovic.raytracer.materials.Metal;

public class Scene {

    private static final Logger logger = Logger.getLogger(Scene.class.getName());

    private final HittableList world;

    private Scene(HittableList world) {
        this.world = world;
    }

    public HittableList world() {
        return world;
    }

    public Color[] render(int image_width, int image_height, Camera camera) throws InterruptedException, ExecutionException {
        return render(image_width, image_height, camera, /*samples_per_pixel=*/100, /*max_depth=*/50);
    }

    public Color[] render(int image_width, int image_height, Camera camera, int samples_per_pixel, int max_depth) throws InterruptedException, ExecutionException {
        if (image_height > 10) {
            logger.log(Level.INFO, "Render");
        }

        Color[] image = new Color[image_width * image_height];

        BVHNode tree = new BVHNode(world.clone(), camera.shutter_open(), camera.shutter_close());

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask[] tasks = new ForkJoinTask[image_height];

        for (int jo = image_height-1; jo >= 0; --jo) {
            final int j = jo;
            tasks[j] = pool.submit(new Callable() {
                public Long call() {
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < image_width; ++i) {
                        Vec3 pixel_color = new Vec3(0, 0, 0);
                        for (int s = 0; s < samples_per_pixel; ++s) {
                            double u = ((double)i + ThreadLocalRandom.current().nextDouble()) / (image_width-1);
                            double v = ((double)j + ThreadLocalRandom.current().nextDouble()) / (image_height-1);
                            pixel_color = pixel_color.add(ray_color(camera.ray(u, v), tree, max_depth));
                        }
                        image[j * image_width + i] = new Color(pixel_color.div(samples_per_pixel).sqrt());
                    }
                    long end = System.currentTimeMillis();
                    return end - start;
                }
            });
        }

        long duration = 0;
        for (int j = image_height - 1; j >= 0; --j) {
            duration += (Long)tasks[j].get();
            if (image_height > 10 && j % (image_height / 10) == 0) {
                logger.log(Level.INFO, String.format("Remaining: %d (~%dus/pixel)", j, duration * 1000 / image_width / (image_height - j)));
            }
        }

        return image;
    }

    public Color ray_color(Ray ray, BVHNode tree, int depth) {
        // If we've exceeded the ray bounce limit, no more light is gathered.
        if (depth <= 0) {
            return new Color(0, 0, 0);
        }

        HitRecord[] rec = new HitRecord[1];
        if (tree.hit(ray, 0.001, Double.POSITIVE_INFINITY, rec)) {
            Color[] attenuation = new Color[1];
            Ray[] scattered = new Ray[1];
            if (rec[0].material().scatter(ray, rec[0], attenuation, scattered)) {
                return new Color(attenuation[0].mul(ray_color(scattered[0], tree, depth - 1)));
            }
            return new Color(0, 0, 0);
        }

        Vec3 unit_direction = ray.direction().unit();
        double t = 0.5 * (unit_direction.y() + 1.0);
        return new Color(new Color(1.0, 1.0, 1.0).mul(1.0 - t).add(new Color(0.5, 0.7, 1.0).mul(t)));
    }

    public static Scene random() {
        Random rand = new Random(0);

        HittableList world = new HittableList();

        Material ground_material = new Metal(new Color(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Point3(0,-1000,0), 1000, ground_material));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double choose_mat = rand.nextDouble();
                Point3 center0 = new Point3(a + 0.9*rand.nextDouble(), 0.2, b + 0.9*rand.nextDouble());

                if (center0.sub(new Point3(4, 0.2, 0)).length() > 0.9) {
                    Material sphere_material;

                    if (choose_mat < 0.8) {
                        // diffuse
                        Color albedo = new Color(Vec3.random(rand).mul(Vec3.random(rand)));
                        sphere_material = new Lambertian(albedo);
                        if (rand.nextInt(4) == 0) {
                            // moving sphere
                            Point3 center1 = new Point3(center0.add(new Vec3(0, 0.5*rand.nextDouble(), 0)));
                            world.add(new MovingSphere(center0, center1, 0.0, 1.0, 0.2, sphere_material));
                        } else {
                            // shpere
                            world.add(new Sphere(center0, 0.2, sphere_material));
                        }
                    } else if (choose_mat < 0.95) {
                        // metal
                        Color albedo = new Color(Vec3.random(rand, 0.5, 1.0));
                        double fuzz = rand.nextDouble() * 0.5;
                        sphere_material = new Metal(albedo, fuzz);
                        world.add(new Sphere(center0, 0.2, sphere_material));
                    } else {
                        // glass
                        sphere_material = new Dielectric(1.5);
                        world.add(new Sphere(center0, 0.2, sphere_material));
                    }
                }
            }
        }

        Material material1 = new Dielectric(1.5);
        world.add(new Sphere(new Point3(0, 1, 0), 1.0, material1));

        Material material2 = new Lambertian(new Color(0.4, 0.2, 0.1));
        world.add(new Sphere(new Point3(-4, 1, 0), 1.0, material2));

        Material material3 = new Metal(new Color(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Point3(4, 1, 0), 1.0, material3));

        return new Scene(world);
    }
}
