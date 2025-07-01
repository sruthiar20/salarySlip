package com.appleUniform.au.bill.service;

import com.appleUniform.au.bill.model.JobCard;
import com.appleUniform.au.bill.model.Worker;
import com.appleUniform.au.bill.repository.JobCardRepository;
import com.appleUniform.au.bill.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;
    
    @Autowired
    private JobCardRepository jobCardRepository;

    public Worker addWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerById(String id) {
        return workerRepository.findById(id).orElse(null);
    }
    
    public Worker updateWorkerAdvance(String id, Double advance) {
        System.out.println("WorkerService: Updating worker " + id + " with advance " + advance);
        Optional<Worker> workerOpt = workerRepository.findById(id);
        if (workerOpt.isPresent()) {
            Worker worker = workerOpt.get();
            System.out.println("WorkerService: Found worker " + worker.getWorkerId() + ", current advance: " + worker.getAdvance());
            worker.setAdvance(advance);
            Worker savedWorker = workerRepository.save(worker);
            System.out.println("WorkerService: Saved worker " + savedWorker.getWorkerId() + ", new advance: " + savedWorker.getAdvance());
            return savedWorker;
        }
        System.out.println("WorkerService: Worker " + id + " not found");
        return null;
    }
    
    public Worker updateWorkerFinances(String workerId, Double advanceAmount, Double advanceDetected, 
                                      Double availableBalance, Double totalAmountToBePaid) {
        System.out.println("Updating finances for worker: " + workerId);
        
        // Update worker
        Worker worker = updateWorkerAdvance(workerId, advanceAmount);
        if (worker == null) {
            return null;
        }
        
        // Update latest job card for this worker
        List<JobCard> workerCards = jobCardRepository.findByWorkerIdOrderByDateDesc(workerId);
        if (!workerCards.isEmpty()) {
            JobCard latestCard = workerCards.get(0); // Get the latest job card
            System.out.println("Found job card: " + latestCard.getId() + " for worker: " + workerId);
            
            latestCard.setAdvance(advanceAmount);
            latestCard.setDetectedAdvance(advanceDetected);
            latestCard.setAdvanceBalance(availableBalance);
            
            // Calculate finalPay based on total and detected advance
            double finalPay = latestCard.getTotal() - advanceDetected;
            latestCard.setFinalPay(finalPay);
            
            jobCardRepository.save(latestCard);
            System.out.println("Updated job card with advance info: " + latestCard.getId());
        } else {
            System.out.println("No job cards found for worker: " + workerId);
        }
        
        return worker;
    }
}