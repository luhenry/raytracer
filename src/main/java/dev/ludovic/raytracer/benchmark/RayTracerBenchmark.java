
package dev.ludovic.raytracer.benchmark;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.ExecutionException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import dev.ludovic.raytracer.*;

@State(Scope.Benchmark)
public class RayTracerBenchmark {

    private final Scene scene = Scene.random();
    private final double aspect_ratio = 3.0 / 2.0;
    private final int image_width = 300;
    private final int image_height = (int)(image_width / aspect_ratio);

    @Benchmark
    public void render(Blackhole bh) throws InterruptedException, ExecutionException {
        bh.consume(scene.render(image_width, image_height));
    }
}
