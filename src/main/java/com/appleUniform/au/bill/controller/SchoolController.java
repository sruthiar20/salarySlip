package com.appleUniform.au.bill.controller;

import com.appleUniform.au.bill.model.Schools;
import com.appleUniform.au.bill.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("appleUniformm/schools")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @GetMapping
    public List<Schools> getAllSchools() {
        return schoolService.getAllSchools();
    }

    @GetMapping("/{id}")
    public Schools getSchoolById(@PathVariable String id) {
        return schoolService.getSchoolById(id);
    }

    @PostMapping
    public Schools addSchool(@RequestBody Schools schools) {
        return schoolService.addSchools(schools);
    }
}