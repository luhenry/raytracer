
package dev.ludovic.raytracer;

public class Ray {

    private final Point3 origin;
    private final Vec3 direction;
    private final double time;

    public Ray(Point3 origin, Vec3 direction) {
        this(origin, direction, 0.0);
    }

    public Ray(Point3 origin, Vec3 direction, double time) {
        this.origin = origin;
        this.direction = direction;
        this.time = time;
    }

    public Point3 origin() {
        return origin;
    }

    public Vec3 direction() {
        return direction;
    }

    public double time() {
        return time;
    }

    public Point3 apply(double t) {
        return new Point3(origin.add(direction.mul(t)));
    }
}
