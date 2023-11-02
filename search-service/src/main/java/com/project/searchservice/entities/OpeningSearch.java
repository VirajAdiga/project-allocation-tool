package com.project.searchservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "opening")
public class OpeningSearch {

    @Id
    private Long id;
    private String title;
    private String details;
    private Integer level;
    private String location;
    private String status;
    private String projectName;
    private List<String> skills;
}
