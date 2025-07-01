package com.appleUniform.au.bill.repository;
import com.appleUniform.au.bill.model.JobCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface JobCardRepository extends MongoRepository<JobCard, String> {
    List<JobCard> findByWorkerId(String workerId);
    List<JobCard> findByWorkerIdOrderByDateDesc(String workerId);
    
    // The default between query in MongoDB doesn't include boundary dates,
    // so we need a custom query that explicitly includes them
    @Query("{'workerId': ?0, 'date': {'$gte': ?1, '$lte': ?2}}")
    List<JobCard> findByWorkerIdAndDateBetween(String workerId, LocalDate startDate, LocalDate endDate);
}