
package dev.ludovic.raytracer;

public class Camera {

    private final Point3 origin;
    private final Point3 lower_left_corner;
    private final Vec3 horizontal;
    private final Vec3 vertical;

    public Camera() {
        double aspect_ratio = 16.0 / 9.0;
        double viewport_height = 2.0;
        double viewport_width = aspect_ratio * viewport_height;
        double focal_length = 1.0;

        origin = new Point3(0, 0, 0);
        horizontal = new Vec3(viewport_width, 0, 0);
        vertical = new Vec3(0, viewport_height, 0);
        lower_left_corner = new Point3(origin.sub(horizontal.div(2)).sub(vertical.div(2)).sub(new Vec3(0, 0, focal_length)));
    }

    public Ray ray(double u, double v) {
        return new Ray(origin, lower_left_corner.add(horizontal.mul(u)).add(vertical.mul(v)).sub(origin));
    }
}