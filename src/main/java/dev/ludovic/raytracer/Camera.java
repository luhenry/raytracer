
package dev.ludovic.raytracer;

public class Camera {

    private final Point3 origin;
    private final Point3 lower_left_corner;
    private final Vec3 horizontal;
    private final Vec3 vertical;
    private final Vec3 u, v, w;
    private final double lens_radius;

    public Camera(Point3 lookfrom, Point3 lookat, Vec3 vup, double vfov /* vertical field-of-view in degrees */, double aspect_ratio, double aperture, double focus_dist) {
        double theta = Math.toRadians(vfov);
        double h = Math.tan(theta / 2);
        double viewport_height = 2.0 * h;
        double viewport_width = aspect_ratio * viewport_height;

        w = lookfrom.sub(lookat).unit();
        u = vup.cross(w).unit();
        v = w.cross(u);

        origin = lookfrom;
        horizontal = u.mul(viewport_width).mul(focus_dist);
        vertical = v.mul(viewport_height).mul(focus_dist);
        lower_left_corner = new Point3(origin.sub(horizontal.div(2)).sub(vertical.div(2)).sub(w.mul(focus_dist)));

        lens_radius = aperture / 2;
    }

    public Ray ray(double s, double t) {
        Vec3 rd = Vec3.randomInUnitDisk().mul(lens_radius);
        Vec3 offset = u.mul(rd.x()).add(v.mul(rd.y()));
        return new Ray(new Point3(origin.add(offset)), lower_left_corner.add(horizontal.mul(s)).add(vertical.mul(t)).sub(origin).sub(offset));
    }
}