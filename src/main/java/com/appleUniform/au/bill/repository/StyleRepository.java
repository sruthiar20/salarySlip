package com.appleUniform.au.bill.repository;

import com.appleUniform.au.bill.model.Style;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StyleRepository extends MongoRepository<Style, String> {
}