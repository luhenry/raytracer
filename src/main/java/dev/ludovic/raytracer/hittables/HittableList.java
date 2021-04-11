
package dev.ludovic.raytracer.hittables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dev.ludovic.raytracer.Ray;

public final class HittableList implements Hittable {

    private final List<Hittable> objects;

    public HittableList() {
        this.objects = new ArrayList<Hittable>();
    }

    private HittableList(List<Hittable> objects) {
        this.objects = objects;
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    public void clear() {
        objects.clear();
    }

    public HittableList clone() {
        return new HittableList((List<Hittable>)objects.clone());
    }

    public Hittable get(int index) {
        return objects.get(index);
    }

    public int size() {
        return objects.size();
    }

    public void sort(Comparator<Hittable> c) {
        objects.sort(c);
    }

    public HittableList subList(int fromIndex, int toIndex) {
        return new HittableList(objects.subList(fromIndex, toIndex));
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

    public boolean bounding_box(double time0, double time1, AABB[] output_box) {
        assert output_box.length == 1;

        if (objects.isEmpty()) {
            return false;
        }

        boolean first_box = true;
        AABB[] temp = new AABB[1];

        for (Hittable object : objects) {
            if (!object.bounding_box(time0, time1, temp)) {
                return false;
            }

            output_box[0] = first_box ? temp[0] : AABB.surrounding_box(output_box[0], temp[0]);
            first_box = false;
        }

        return true;
    }
}
