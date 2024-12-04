package com.rossie.websortingalgorithm.controller;

import com.rossie.websortingalgorithm.HateoasGenerator;
import com.rossie.websortingalgorithm.model.DataModel;
import com.rossie.websortingalgorithm.model.SortRequest;
import com.rossie.websortingalgorithm.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class Controller {
    private final DataService dataService;
    private final HateoasGenerator hateoasGenerator;

    @GetMapping("/data")
    @ResponseBody
    public List<DataModel> getAllData(){
        return dataService.getAllData();
    }

    @GetMapping("/data/{id}")
    @ResponseBody
    public DataModel getDataById(@PathVariable("id") int id){
        DataModel data = dataService.getDataById(id);
        hateoasGenerator.addHateoasLinks(data);
        return data;
    }

    @ResponseBody
    @GetMapping("algorithm/{algorithm}")
    public List<DataModel> getDataByAlgorithm(@PathVariable("algorithm") String algorithm){
        return dataService.getDataByAlgorithm(algorithm);
    }

    @ResponseBody
    @PostMapping("/data/add")
    public DataModel createData(@RequestBody List<Integer> list){
        DataModel dataModel = dataService.createData(list);
        hateoasGenerator.addHateoasLinks(dataModel);
        return (DataModel) ResponseEntity.ok(dataModel).getBody();
    }

    @PostMapping("/sort")
    @ResponseBody
    public ResponseEntity<?> sortData(@RequestBody SortRequest request) {
        DataModel sortedData = dataService.sort(request.getId(), request.getAlgorithm());
        hateoasGenerator.addHateoasLinks(sortedData);
        return ResponseEntity.ok(sortedData);
    }

    @ResponseBody
    @DeleteMapping("data/{id}/delete")
    public ResponseEntity<String> deleteData(@PathVariable("id") int id){
        dataService.deleteData(id);
        return ResponseEntity.ok("Data Deleted");
    }

}
