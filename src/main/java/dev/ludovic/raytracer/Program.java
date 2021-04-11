
package dev.ludovic.raytracer;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Program {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        int image_width = 1920;
        int image_height = 1080;
        Color[] image = Scene.random().render(image_width, image_height,
                            new Camera(new Point3(13, 2, 3), new Point3(0, 0, 0), new Vec3(0, 1, 0),
                                20, ((double)image_width) / image_height, 0.1, 10.0, 0.0, 1.0));

        // Print
        try (FileOutputStream file = new FileOutputStream("output.ppm");
             DataOutputStream out = new DataOutputStream(file)) {
            out.writeBytes(String.format("P6\n"));
            out.writeBytes(String.format("%d %d\n", image_width, image_height));
            out.writeBytes(String.format("255\n"));

            for (int j = image_height-1; j >= 0; --j) {
                for (int i = 0; i < image_width; ++i) {
                    Color color = image[j * image_width + i];
                    out.writeByte((int)(color.x() * 255));
                    out.writeByte((int)(color.y() * 255));
                    out.writeByte((int)(color.z() * 255));
                }
            }

            out.flush();
        }
    }
}
