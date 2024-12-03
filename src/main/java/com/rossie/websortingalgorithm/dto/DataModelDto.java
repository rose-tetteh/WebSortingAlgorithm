package com.rossie.websortingalgorithm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataModelDto extends RepresentationModel<DataModelDto> {
    private int id;
    private List<Integer> list;
    private String sortAlgorithm;
}
