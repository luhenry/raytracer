
package dev.ludovic.raytracer.hittables;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import dev.ludovic.raytracer.Ray;

public class BVHNode implements Hittable {

    private final Hittable left;
    private final Hittable right;
    private final AABB box;

    public BVHNode(HittableList list, double time0, double time1) {
        final int axis = ThreadLocalRandom.current().nextInt(0, 3);
        Comparator<Hittable> comparator = Comparator.comparingDouble(value -> {
            AABB[] box = new AABB[1];
            if (!value.bounding_box(0, 0, box)) {
                throw new UnsupportedOperationException("No bounding box");
            }
            return box[0].minimum().apply(axis);
        });

        switch (list.size()) {
        case 1:
            this.left = this.right = list.get(0);
            break;
        case 2:
            if (comparator.compare(list.get(0), list.get(1)) >= 0) {
                this.left = list.get(0);
                this.right = list.get(1);
            } else {
                this.left = list.get(1);
                this.right = list.get(0);
            }
            break;
        default:
            list.sort(comparator);
            this.left = new BVHNode(list.subList(0, list.size() / 2).clone(), time0, time1);
            this.right = new BVHNode(list.subList(list.size() / 2, list.size()).clone(), time0, time1);
            break;
        }

        AABB[] box_left = new AABB[1];
        AABB[] box_right = new AABB[1];
        if (!this.left.bounding_box(time0, time1, box_left) || !this.right.bounding_box(time0, time1, box_right)) {
            throw new UnsupportedOperationException("No bounding box");
        }

        this.box = AABB.surrounding_box(box_left[0], box_right[0]);
    }

    public boolean hit(Ray ray, double t_min, double t_max, HitRecord[] rec) {
        if (!box.hit(ray, t_min, t_max)) {
            return false;
        }

        boolean hit_left = left.hit(ray, t_min, t_max, rec);
        boolean hit_right = right.hit(ray, t_min, hit_left ? rec[0].t() : t_max, rec);

        return hit_left || hit_right;
    }

    public boolean bounding_box(double time0, double time1, AABB[] output_box) {
        output_box[0] = box;
        return true;
    }
}
