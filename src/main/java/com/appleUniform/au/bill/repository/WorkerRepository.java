package com.appleUniform.au.bill.repository;

import com.appleUniform.au.bill.model.Worker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkerRepository extends MongoRepository<Worker, String> {
}