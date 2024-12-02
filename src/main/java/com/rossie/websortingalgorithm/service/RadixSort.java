package com.rossie.websortingalgorithm.service;

import java.util.Collections;
import java.util.List;

/**
 * The type Radix sort.
 */
public class RadixSort implements SortAlgorithm {
    @Override
    public List<Integer> sort(List<Integer> data) {
        int max = Collections.max(data);
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(data, exp);
        }
        return data;
    }

    private void countingSort(List<Integer> array, int exp) {
        int n = array.size();
        int[] output = new int[n];
        int[] count = new int[10];

        for (Integer integer : array)
            count[(integer / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];


        for (int i = n - 1; i >= 0; i--) {
            output[count[(array.get(i) / exp) % 10] - 1] = array.get(i);
            count[(array.get(i) / exp) % 10]--;
        }

        for (int i = 0; i < n; i++) {
            array.set(i, output[i]);
        }
    }
}
