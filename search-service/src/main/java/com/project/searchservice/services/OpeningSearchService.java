package com.project.searchservice.services;

import com.project.searchservice.dto.OpeningSearchList;
import com.project.searchservice.entities.OpeningSearch;
import com.project.searchservice.exception.ServerSideGeneralException;
import com.project.searchservice.repositories.OpeningSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class OpeningSearchService {

    @Autowired
    private OpeningSearchRepository openingSearchRepository;

    public OpeningSearch createOpeningForSearch(OpeningSearch openingSearch){
        try{
            return openingSearchRepository.save(openingSearch);
        }catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    public void deleteOpening(OpeningSearch openingSearch){
        try{
            openingSearchRepository.delete(openingSearch);
        }catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }

    public OpeningSearchList getAllMatchingOpenings(Integer pageSize, Integer pageNumber, String textToMatch) throws IOException {
        if (pageSize == null) pageSize = 10;
        if (pageNumber == null) pageNumber = 0;
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        try {
            Page<OpeningSearch> openingSearches = openingSearchRepository.findByTitleOrSkillsWithFuzzy(textToMatch, pageable);
            List<OpeningSearch> openingSearchList = openingSearches.getContent();
            return OpeningSearchList.builder().
                    openings(openingSearchList).
                    totalElements(openingSearchList.size()).
                    build();
        }
        catch (Exception exception){
            throw new ServerSideGeneralException("Something went wrong!");
        }
    }
}
