package com.appleUniform.au.bill.repository;
import com.appleUniform.au.bill.model.JobCard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobCardRepository extends MongoRepository<JobCard, String> {
    List<JobCard> findByWorkerId(String workerId);
    List<JobCard> findByWorkerIdOrderByDateDesc(String workerId);
}