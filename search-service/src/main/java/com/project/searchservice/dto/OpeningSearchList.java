package com.project.searchservice.dto;

import com.project.searchservice.entities.OpeningSearch;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OpeningSearchList {

    List<OpeningSearch> openings;
    Integer totalElements;
}
