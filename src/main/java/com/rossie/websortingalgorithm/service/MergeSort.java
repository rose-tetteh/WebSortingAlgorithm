package com.rossie.websortingalgorithm.service;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Merge sort.
 */
public class MergeSort implements SortAlgorithm {
    @Override
    public List<Integer> sort(List<Integer> data) {
        mergeSort(data, 0, data.size() - 1);
        return data;
    }

    private void mergeSort(List<Integer> array, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);

            merge(array, left, middle, right);
        }
    }

    private void merge(List<Integer> array, int left, int middle, int right) {
        List<Integer> leftList = new ArrayList<>(array.subList(left, middle + 1));
        List<Integer> rightList = new ArrayList<>(array.subList(middle + 1, right + 1));

        int i = 0, j = 0, k = left;

        while (i < leftList.size() && j < rightList.size()) {
            if (leftList.get(i) <= rightList.get(j)) {
                array.set(k++, leftList.get(i++));
            } else {
                array.set(k++, rightList.get(j++));
            }
        }

        while (i < leftList.size()) {
            array.set(k++, leftList.get(i++));
        }

        while (j < rightList.size()) {
            array.set(k++, rightList.get(j++));
        }
    }
}
