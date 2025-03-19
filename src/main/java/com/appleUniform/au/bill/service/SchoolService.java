package com.appleUniform.au.bill.service;


import com.appleUniform.au.bill.model.Schools;
import com.appleUniform.au.bill.model.Worker;
import com.appleUniform.au.bill.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    public List<Schools> getAllSchools() {
        return schoolRepository.findAll();
    }

    public Schools getSchoolById(String schoolId) {
        return schoolRepository.findById(schoolId).orElse(null);
    }
    public Schools addSchools(Schools schools) {
        return schoolRepository.save(schools);
    }

}