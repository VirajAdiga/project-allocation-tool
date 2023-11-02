package com.project.searchservice.controllers;

import com.project.searchservice.dto.OpeningSearchList;
import com.project.searchservice.services.OpeningSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/search")
public class OpeningSearchController {

    @Autowired
    private OpeningSearchService openingSearchService;

    @GetMapping("")
    public ResponseEntity<OpeningSearchList> getAllMatchingOpenings(@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(name = "searchText") String searchText) throws IOException {
        return ResponseEntity.ok(openingSearchService.getAllMatchingOpenings(pageSize, pageNumber, searchText));
    }
}
