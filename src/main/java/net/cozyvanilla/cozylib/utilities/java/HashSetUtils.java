package net.cozyvanilla.cozylib.utilities.java;

import java.util.HashSet;
import java.util.Set;

public class HashSetUtils {

    /**
     * Returns the intersection of two sets (elements present in both sets).
     *
     * @param a the first set
     * @param b the second set
     * @return a new set containing common elements from both sets
     */
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    /**
     * Returns the union of two sets (all unique elements from both sets).
     *
     * @param a the first set
     * @param b the second set
     * @return a new set containing all elements from both sets
     */
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }

    /**
     * Returns the difference of two sets (elements in a that are not in b).
     *
     * @param a the first set
     * @param b the second set
     * @return a new set containing elements from a excluding those in b
     */
    public static <T> Set<T> difference(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }
}