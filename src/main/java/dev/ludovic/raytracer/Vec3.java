
package dev.ludovic.raytracer;

import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Vec3 {

    private final double x;
    private final double y;
    private final double z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return x;
    }
    public Vec3 x(double x) {
        return new Vec3(x, this.y, this.z);
    }

    public double y() {
        return y;
    }
    public Vec3 y(double y) {
        return new Vec3(this.x, y, this.z);
    }

    public double z() {
        return z;
    }
    public Vec3 z(double z) {
        return new Vec3(this.x, this.y, z);
    }

    public Vec3 neg() {
        return new Vec3(-x, -y, -z);
    }

    public Vec3 add(Vec3 that) {
        return new Vec3(x + that.x, y + that.y, z + that.z);
    }

    public Vec3 add(double value) {
        return new Vec3(x + value, y + value, z + value);
    }

    public Vec3 sub(Vec3 that) {
        return new Vec3(x - that.x, y - that.y, z - that.z);
    }

    public Vec3 sub(double value) {
        return new Vec3(x - value, y - value, z - value);
    }

    public Vec3 mul(Vec3 that) {
        return new Vec3(x * that.x, y * that.y, z * that.z);
    }

    public Vec3 mul(double value) {
        return new Vec3(x * value, y * value, z * value);
    }

    public Vec3 div(Vec3 that) {
        return new Vec3(x / that.x, y / that.y, z / that.z);
    }

    public Vec3 div(double value) {
        return new Vec3(x / value, y / value, z / value);
    }

    public Vec3 sqrt() {
        return new Vec3(Math.sqrt(x), Math.sqrt(y), Math.sqrt(z));
    }

    public double dot(Vec3 that) {
        return x * that.x
             + y * that.y
             + z * that.z;
    }

    public Vec3 cross(Vec3 that) {
        return new Vec3(
            y * that.z - z * that.y,
            z * that.x - x * that.z,
            x * that.y - y * that.x
        );
    }

    public Vec3 unit() {
        double len = length();
        if (len == 0.0) {
            return this;
        }
        return div(len);
    }

    public double apply(int i) {
        switch(i) {
        case 0: return x;
        case 1: return y;
        case 2: return z;
        default: throw new IndexOutOfBoundsException();
        }
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return this.dot(this);
    }

    public boolean nearZero() {
        // Return true if the vector is close to zero in all dimensions.
        final double s = 1e-8;
        return (Math.abs(x) < s) && (Math.abs(y) < s) && (Math.abs(z) < s);
    }

    public Vec3 reflect(Vec3 normal) {
        return this.sub(normal.mul(2 * this.dot(normal)));
    }

    public Vec3 refract(Vec3 normal, double etai_over_etat) {
        double cos_theta = Math.min(this.neg().dot(normal), 1.0);
        Vec3 r_out_perp = this.add(normal.mul(cos_theta)).mul(etai_over_etat);
        Vec3 r_out_parallel = normal.mul(-Math.sqrt(Math.abs(1.0 - r_out_perp.lengthSquared())));
        return r_out_perp.add(r_out_parallel);
    }

    public static Vec3 random(Random rand, double min, double max) {
        return new Vec3(min + rand.nextDouble() * (max - min), min + rand.nextDouble() * (max - min), min + rand.nextDouble() * (max - min));
    }

    public static Vec3 random(double min, double max) {
        return random(ThreadLocalRandom.current(), min, max);
    }

    public static Vec3 random(Random rand) {
        return random(rand, 0.0, 1.0);
    }

    public static Vec3 random() {
        return random(0.0, 1.0);
    }

    public static Vec3 randomInUnitSphere() {
        while (true) {
            Vec3 p = random(-1, 1);
            if (p.lengthSquared() >= 1) continue;
            return p;
        }
    }

    public static Vec3 randomInUnitDisk() {
        while (true) {
            Vec3 p = random(-1, 1).z(0);
            if (p.lengthSquared() >= 1) continue;
            return p;
        }
    }

    public static Vec3 randomInHemisphere(Vec3 normal) {
        Vec3 in_unit_sphere = randomInUnitSphere();
        // In the same hemisphere as the normal
        if (in_unit_sphere.dot(normal) > 0.0)
            return in_unit_sphere;
        else
            return in_unit_sphere.neg();
    }

    public static Vec3 randomUnit() {
        return randomInUnitSphere().unit();
    }

    @Override
    public String toString() {
        return String.format("%f %f %f", x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Vec3))
            return false;
        Vec3 that = (Vec3) o;
        return x == that.x && y == that.y && z == that.z;
    }
}
