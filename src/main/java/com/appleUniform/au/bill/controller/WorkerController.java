package com.appleUniform.au.bill.controller;

import com.appleUniform.au.bill.model.Worker;
import com.appleUniform.au.bill.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @PostMapping("/appleUniformm/workers")
    public Worker addWorker(@RequestBody Worker worker) {
        return workerService.addWorker(worker);
    }

    @GetMapping("/appleUniformm/workers")
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }
    
    @GetMapping("/appleUniformm/workers/{id}")
    public ResponseEntity<Worker> getWorkerById(@PathVariable String id) {
        Worker worker = workerService.getWorkerById(id);
        if (worker != null) {
            return ResponseEntity.ok(worker);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PatchMapping("/appleUniformm/workers/{id}/advance")
    public ResponseEntity<Worker> updateWorkerAdvance(@PathVariable String id, @RequestBody Map<String, Double> advanceUpdate) {
        Double advance = advanceUpdate.get("advance");
        if (advance == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Worker updatedWorker = workerService.updateWorkerAdvance(id, advance);
        if (updatedWorker != null) {
            return ResponseEntity.ok(updatedWorker);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/appleUniformm/workers/update-finances")
    public ResponseEntity<Worker> updateWorkerBalance(@RequestBody Map<String, Object> balanceData) {
        System.out.println("Received financial data: " + balanceData);
        
        String workerId = (String) balanceData.get("workerId");
        
        // Extract all financial data
        Double advanceAmount = getDoubleValue(balanceData, "advanceAmount");
        Double advanceDetected = getDoubleValue(balanceData, "advanceDetected"); 
        Double availableBalance = getDoubleValue(balanceData, "availableBalance");
        Double totalAmountToBePaid = getDoubleValue(balanceData, "totalAmountToBePaid");
        
        System.out.println("Worker ID: " + workerId + ", Advance: " + advanceAmount);
        
        if (workerId == null || advanceAmount == null) {
            System.out.println("Bad request - workerId: " + workerId + ", advance: " + advanceAmount);
            return ResponseEntity.badRequest().build();
        }
        
        Worker updatedWorker = workerService.updateWorkerFinances(
            workerId, advanceAmount, advanceDetected, availableBalance, totalAmountToBePaid);
            
        if (updatedWorker != null) {
            System.out.println("Worker updated successfully: " + updatedWorker.getWorkerId() + ", Advance: " + updatedWorker.getAdvance());
            return ResponseEntity.ok(updatedWorker);
        }
        
        System.out.println("Worker not found: " + workerId);
        return ResponseEntity.notFound().build();
    }
    
    private Double getDoubleValue(Map<String, Object> data, String key) {
        if (data.get(key) != null) {
            Object value = data.get(key);
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else if (value instanceof String) {
                try {
                    return Double.valueOf((String) value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
}