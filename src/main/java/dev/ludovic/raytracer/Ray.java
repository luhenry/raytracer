
package dev.ludovic.raytracer;

public class Ray {

    private final Point3 origin;
    private final Vec3 direction;

    public Ray(Point3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Point3 origin() {
        return origin;
    }

    public Vec3 direction() {
        return direction;
    }

    public Point3 apply(double t) {
        return new Point3(origin.add(direction.mul(t)));
    }
}
