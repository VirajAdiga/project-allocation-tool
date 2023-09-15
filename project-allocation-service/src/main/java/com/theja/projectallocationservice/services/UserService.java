package com.theja.projectallocationservice.services;

import com.theja.projectallocationservice.models.DBUser;
import com.theja.projectallocationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieve a page of users from the free pool.
     *
     * @param pageSize   The maximum number of users per page.
     * @param pageNumber The page number to retrieve (0-based).
     * @return A page of free pool users.
     */
    public Page<DBUser> getFreePoolUsers(Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.getFreePoolUsers(pageable);
    }

    /**
     * Retrieve a page of users allocated to projects within a specified date range.
     *
     * @param startDate  The start date of the allocation period.
     * @param endDate    The end date of the allocation period.
     * @param pageSize   The maximum number of users per page.
     * @param pageNumber The page number to retrieve (0-based).
     * @return A page of allocated users within the specified date range.
     */
    public Page<DBUser> getAllAllocatedUsers(Date startDate, Date endDate, Integer pageSize, Integer pageNumber) {
        if (pageSize == null) pageSize = 1000;
        if (pageNumber == null) pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.getAllAllocatedUsers(startDate, endDate, pageable);
    }
}
