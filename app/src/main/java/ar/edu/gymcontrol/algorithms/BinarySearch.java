package ar.edu.gymcontrol.algorithms;

import java.util.Comparator;
import java.util.List;

public class BinarySearch {
    public static <T> int indexOf(List<T> sorted, T target, Comparator<? super T> cmp) {
        int lo = 0, hi = sorted.size() - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int c = cmp.compare(sorted.get(mid), target);
            if (c == 0) return mid;
            if (c < 0) lo = mid + 1; else hi = mid - 1;
        }
        return -1;
    }
}
