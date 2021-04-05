
package dev.ludovic.raytracer;

import java.util.ArrayList;

public class HittableList implements Hittable {

    private final ArrayList<Hittable> objects = new ArrayList<Hittable>();

    public HittableList() {}

    public HittableList(Hittable object) {
        add(object);
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    public void clear() {
        objects.clear();
    }

    public boolean hit(Ray ray, double t_min, double t_max, HitRecord[] rec) {
        assert rec.length == 1;

        boolean hit_anything = false;
        double closest_so_far = t_max;

        for (Hittable object : objects) {
            HitRecord[] temp_rec = new HitRecord[1];
            if (object.hit(ray, t_min, closest_so_far, temp_rec)) {
                hit_anything = true;
                closest_so_far = temp_rec[0].t();
                rec[0] = temp_rec[0];
            }
        }

        return hit_anything;
    }
}
