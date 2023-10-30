package com.project.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningSearchMessage {

    private Long id;
    private String title;
    private String details;
    private Integer level;
    private String location;
    private String status;
    private String projectName;
    private List<String> skills;
    private String actionType;

}
