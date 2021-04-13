
package dev.ludovic.raytracer.benchmark;

import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import dev.ludovic.raytracer.*;
import dev.ludovic.raytracer.hittables.BVHNode;

@State(Scope.Benchmark)
public class RayTracerBenchmark {

    private final Scene scene = Scene.random();

    private final Random rand = new Random(0);

    private Camera camera;
    private BVHNode tree;

    @Param({ "1920" })
    public int width;

    @Param({ "1080" })
    public int height;

    @Setup
    public void setup() {
        camera = new Camera(new Point3(13, 2, 3), new Point3(0, 0, 0), new Vec3(0, 1, 0), 20, ((double)width) / height, 0.1, 10.0, 0.0, 1.0);
        tree = new BVHNode(scene.world(), camera.shutter_open(), camera.shutter_close());
    }

    @Benchmark
    public void render(Blackhole bh) {
        bh.consume(scene.ray_color(camera.ray(rand.nextDouble(), rand.nextDouble()), tree, 50));
    }
}
