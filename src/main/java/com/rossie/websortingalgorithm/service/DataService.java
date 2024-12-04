package com.rossie.websortingalgorithm.service;

import com.rossie.websortingalgorithm.model.DataModelDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Data service.
 */
@Service
public class DataService {
    private final Map<Integer, DataModelDto> dataStore = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    /**
     * Get all data list.
     *
     * @return the list
     */
    public List<DataModelDto> getAllData(){
        return new ArrayList<>(dataStore.values());
    }

    /**
     * Get data by id data model.
     *
     * @param id the id
     * @return the data model
     */
    public DataModelDto getDataById(int id){
        return dataStore.get(id);
    }

    /**
     * Get data by algorithm list.
     *
     * @param algorithm the algorithm
     * @return the list
     */
    public List<DataModelDto> getListOfDataByAlgorithm(String algorithm){
        List<DataModelDto> result = new ArrayList<>();
        for (DataModelDto data : dataStore.values()){
            if (algorithm.equals(data.getSortAlgorithm())){
                result.add(data);
            }
        }
        return result;
    }

    /**
     * Create data data model.
     *
     * @param list the list
     * @return the data model
     */
    public DataModelDto createData(List<Integer> list){
        int id = idCounter.getAndIncrement();
        DataModelDto data = new DataModelDto();
        data.setId(id);
        data.setList(list);
        data.setSortAlgorithm(null);
        data.setSortedList(null);
        dataStore.put(id, data);
        return data;
    }

    /**
     * Delete data.
     *
     * @param id the id
     */
    public void deleteData(int id){
        if (!dataStore.containsKey(id)){
            throw new NoSuchElementException("DataModel with ID "+ id + "not found");
        }
        dataStore.remove(id);
    }

    /**
     * Sort data model.
     *
     * @param id        the id
     * @param algorithm the algorithm
     * @return the data model
     */
    public DataModelDto sort(SortRequestDto sortRequestDto) {
        DataModelDto data = dataStore.get(id);
        if (data == null)
            throw new NoSuchElementException("DataModel not found");

        if (algorithm == null)
            throw new IllegalArgumentException("Invalid Sorting Algorithm");

        SortAlgorithm sorter = switch (algorithm.toLowerCase()) {
            case "quicksort" -> new QuickSort();
            case "heapsort" -> new HeapSort();
            case "mergesort" -> new MergeSort();
            case "radixsort" -> new RadixSort();
            case "bucketsort" -> new BucketSort();
            default -> throw new IllegalArgumentException("Unsupported sorting algorithm");
        };
        data.setSortedList(sorter.sort(data.getList()));
        return data;
    }
}
