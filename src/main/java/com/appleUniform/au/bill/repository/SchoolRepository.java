package com.appleUniform.au.bill.repository;

import com.appleUniform.au.bill.model.Schools;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SchoolRepository extends MongoRepository<Schools, String> {
}