
package dev.ludovic.raytracer;

import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;

public class Program {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        double aspect_ratio = 3.0 / 2.0;
        int image_height = 400;
        int image_width = (int)(image_height * aspect_ratio);
        Color[] image = Scene.random().render(image_width, image_height);

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
