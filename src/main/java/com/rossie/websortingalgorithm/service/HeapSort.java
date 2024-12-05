package com.rossie.websortingalgorithm.service;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Heap sort.
 */
public class HeapSort implements SortAlgorithm {
    @Override
    public List<Integer> sort(List<Integer> data) {
        // Create a new list to avoid modifying the original list
        List<Integer> sortedList = new ArrayList<>(data);

        int n = sortedList.size();
        for (int i = n/2 -1; i >= 0; i--)
            heapify(sortedList, n, i);

        for (int i = n-1; i >= 0; i--) {
            int temp = sortedList.get(0);
            sortedList.set(0, sortedList.get(i));
            sortedList.set(i, temp);

            heapify(sortedList, i, 0);
        }

        return sortedList;
    }

    /**
     * Heapify.
     *
     * @param array the array
     * @param n     the n
     * @param i     the
     */
    static void heapify(List<Integer> array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array.get(left) > array.get(largest))
            largest = left;

        if (right < n && array.get(right) > array.get(largest))
            largest = right;

        if (largest != i) {
            int temp = array.get(i);
            array.set(i, array.get(largest));
            array.set(largest, temp);

            heapify(array, n, largest);
        }
    }
}
