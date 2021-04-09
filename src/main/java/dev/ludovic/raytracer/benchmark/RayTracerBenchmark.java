
package dev.ludovic.raytracer.benchmark;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.ExecutionException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import dev.ludovic.raytracer.*;

@State(Scope.Benchmark)
public class RayTracerBenchmark {

    private final Scene scene = Scene.random();

    @Param({ "1.5" })
    public double ratio;

    @Param({ "100" })
    public int height;

    @Benchmark
    public void render(Blackhole bh) throws InterruptedException, ExecutionException {
        bh.consume(scene.render((int)(height * ratio), height));
    }
}
