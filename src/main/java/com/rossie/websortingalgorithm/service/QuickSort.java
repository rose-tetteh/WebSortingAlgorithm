package com.rossie.websortingalgorithm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Quick sort.
 */
public class QuickSort implements SortAlgorithm {

    @Override
    public List<Integer> sort(List<Integer> data) {
        // Create a new list to avoid modifying the original list
        List<Integer> sortedList = new ArrayList<>(data);
        quickSort(sortedList, 0, sortedList.size() - 1);
        return sortedList;
    }

    private void quickSort(List<Integer> array, int low, int high) {
        if (low < high) {
            int part = partition(array, low, high);
            quickSort(array, low, part - 1);
            quickSort(array, part + 1, high);
        }
    }

    /**
     * Partition int.
     *
     * @param array the array
     * @param low   the low
     * @param high  the high
     * @return the int
     */
    static int partition(List<Integer> array, int low, int high) {
        int pivot = array.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array.get(j) <= pivot) {
                i++;
                int temp = array.get(i);
                array.set(i, array.get(j));
                array.set(j, temp);
            }
        }

        int temp = array.get(i + 1);
        array.set(i + 1, array.get(high));
        array.set(high, temp);

        return i + 1;
    }
}
