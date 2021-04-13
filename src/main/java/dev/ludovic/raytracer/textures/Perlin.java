
package dev.ludovic.raytracer.textures;

import java.util.concurrent.ThreadLocalRandom;

import dev.ludovic.raytracer.Point3;
import dev.ludovic.raytracer.Vec3;

public class Perlin {

    private final static int point_count = 256;

    private final Vec3[] ranvec;
    private final int[] perm_x;
    private final int[] perm_y;
    private final int[] perm_z;

    public Perlin() {
        this.ranvec = new Vec3[point_count];
        for (int i = 0; i < point_count; i++) {
            this.ranvec[i] = Vec3.random(-1, 1).unit();
        }

        this.perm_x = generate_perm();
        this.perm_y = generate_perm();
        this.perm_z = generate_perm();
    }

    public double noise(Point3 p) {
        double u = p.x() - Math.floor(p.x());
        double v = p.y() - Math.floor(p.y());
        double w = p.z() - Math.floor(p.z());

        // Hermitian Smoothing
        u = u * u * (3 - 2 * u);
        v = v * v * (3 - 2 * v);
        w = w * w * (3 - 2 * w);

        int i = (int)Math.floor(p.x());
        int j = (int)Math.floor(p.y());
        int k = (int)Math.floor(p.z());

        Vec3[][][] c = new Vec3[2][2][2];
        for (int di = 0; di < 2; di++) {
            for (int dj = 0; dj < 2; dj++) {
                for (int dk = 0; dk < 2; dk++) {
                    c[di][dj][dk] = ranvec[perm_x[(i + di) & 255] ^ perm_y[(j + dj) & 255] ^ perm_z[(k + dk) & 255]];
                }
            }
        }

        return (perlin_interpolation(c, u, v, w) + 1) / 2;
    }

    public double turbulence(Point3 p) {
        return turbulence(p, 7);
    }

    public double turbulence(Point3 p, int depth) {
        double accum = 0.0;
        double weight = 1.0;
        Point3 temp_p = new Point3(p);

        for (int i = 0; i < depth; i++) {
            accum += weight * noise(temp_p);
            weight *= 0.5;
            temp_p = new Point3(temp_p.mul(2));
        }

        return Math.abs(accum);
    }

    private int[] generate_perm() {
        int[] p = new int[point_count];
        for (int i = 0; i < point_count; i++) {
            p[i] = i;
        }
        for (int i = point_count - 1; i > 0; i--) {
            int target = ThreadLocalRandom.current().nextInt(i);
            int tmp = p[i];
            p[i] = p[target];
            p[target] = tmp;
        }
        return p;
    }

    private double perlin_interpolation(Vec3 c[][][], double u, double v, double w) {
        double uu = u * u * (3 - 2 * u);
        double vv = v * v * (3 - 2 * v);
        double ww = w * w * (3 - 2 * w);
        double accum = 0.0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    Vec3 weight_v = new Vec3(u - i, v - j, w - k);
                    accum += (i * uu + (1 - i) * (1 - uu))
                          *  (j * vv + (1 - j) * (1 - vv))
                          *  (k * ww + (1 - k) * (1 - ww))
                          *  c[i][j][k].dot(weight_v);
                }
            }
        }
        return accum;
    }
}
