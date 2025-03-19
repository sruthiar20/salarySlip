package com.appleUniform.au.bill.controller;

import com.appleUniform.au.bill.model.Worker;
import com.appleUniform.au.bill.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("appleUniformm/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @PostMapping
    public Worker addWorker(@RequestBody Worker worker) {
        return workerService.addWorker(worker);
    }

    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }
}