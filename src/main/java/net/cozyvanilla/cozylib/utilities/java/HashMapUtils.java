package net.cozyvanilla.cozylib.utilities.java;

import net.cozyvanilla.cozylib.Enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashMapUtils {

    /**
     * Sorts a map by its Double values based on the given ordering.
     *
     * @param map the map to sort
     * @param order the ordering (ascending or descending)
     */
    public static void sortDouble(Map<String, Double> map, Enums.Ordering order) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());

        list.sort((a, b) -> order.isAscending()
                ? Double.compare(a.getValue(), b.getValue())
                : Double.compare(b.getValue(), a.getValue()));

        map.clear();
        for (Map.Entry<String, Double> entry : list) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sorts a map by its Integer values based on the given ordering.
     *
     * @param map the map to sort
     * @param order the ordering (ascending or descending)
     */
    public static void sortInt(Map<String, Integer> map, Enums.Ordering order) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        list.sort((a, b) -> order.isAscending()
                ? Integer.compare(a.getValue(), b.getValue())
                : Integer.compare(b.getValue(), a.getValue()));

        map.clear();
        for (Map.Entry<String, Integer> entry : list) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sorts a map by its Long values based on the given ordering.
     *
     * @param map the map to sort
     * @param order the ordering (ascending or descending)
     */
    public static void sortLong(Map<String, Long> map, Enums.Ordering order) {
        List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());

        list.sort((a, b) -> order.isAscending()
                ? Long.compare(a.getValue(), b.getValue())
                : Long.compare(b.getValue(), a.getValue()));

        map.clear();
        for (Map.Entry<String, Long> entry : list) {
            map.put(entry.getKey(), entry.getValue());
        }
    }
}