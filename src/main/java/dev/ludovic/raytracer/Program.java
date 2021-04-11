
package dev.ludovic.raytracer;

import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;

public class Program {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int image_width = 1280;
        int image_height = 960;
        Color[] image = Scene.random().render(image_width, image_height,
                            new Camera(new Point3(13, 2, 3), new Point3(0, 0, 0), new Vec3(0, 1, 0),
                                20, ((double)image_width) / image_height, 0.1, 10.0, 0.0, 1.0));

        // Print
        System.out.print ("P3\n");
        System.out.printf("%d %d\n", image_width, image_height);
        System.out.print ("255\n");

        for (int j = image_height-1; j >= 0; --j) {
            for (int i = 0; i < image_width; ++i) {
                System.out.printf("%s\n", image[j * image_width + i]);
            }
        }

        System.out.flush();
    }
}
