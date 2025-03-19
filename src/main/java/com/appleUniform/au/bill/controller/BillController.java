package com.appleUniform.au.bill.controller;

import com.appleUniform.au.bill.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("appleUniformm/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/{workerId}")
    public Map<String, Object> generateBill(@PathVariable String workerId) {
        return billService.generateBill(workerId);
    }
}