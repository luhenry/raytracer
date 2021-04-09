
package dev.ludovic.raytracer;

import java.util.concurrent.ThreadLocalRandom;

public class Camera {

    private final Point3 origin;
    private final Point3 lower_left_corner;
    private final Vec3 horizontal;
    private final Vec3 vertical;
    private final Vec3 u, v, w;
    private final double lens_radius;
    private final double time_0,  time_1; // shutter open/close times

    public Camera(Point3 lookfrom, Point3 lookat, Vec3 vup, double vfov /* vertical field-of-view in degrees */, double aspect_ratio, double aperture, double focus_dist, double time_0, double  time_1) {
        double theta = Math.toRadians(vfov);
        double h = Math.tan(theta / 2);
        double viewport_height = 2.0 * h;
        double viewport_width = aspect_ratio * viewport_height;

        this.w = lookfrom.sub(lookat).unit();
        this.u = vup.cross(w).unit();
        this.v = w.cross(u);

        this.origin = lookfrom;
        this.horizontal = u.mul(viewport_width).mul(focus_dist);
        this.vertical = v.mul(viewport_height).mul(focus_dist);
        this.lower_left_corner = new Point3(origin.sub(horizontal.div(2)).sub(vertical.div(2)).sub(w.mul(focus_dist)));

        this.lens_radius = aperture / 2;

        this.time_0 = time_0;
        this.time_1 = time_1;
    }

    public Ray ray(double s, double t) {
        Vec3 rd = Vec3.randomInUnitDisk().mul(lens_radius);
        Vec3 offset = u.mul(rd.x()).add(v.mul(rd.y()));
        return new Ray(new Point3(origin.add(offset)), lower_left_corner.add(horizontal.mul(s)).add(vertical.mul(t)).sub(origin).sub(offset), ThreadLocalRandom.current().nextDouble(time_0, time_1));
    }
}