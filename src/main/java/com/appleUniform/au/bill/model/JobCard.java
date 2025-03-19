
package com.appleUniform.au.bill.model;

import com.appleUniform.au.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Document(collection = "jobCards")
@Getter
@Setter
public class JobCard {
    @Id
    private String id;
    private String workerId;
    private String styleId;
    private String department;
    private int quantity;
    private double rate;
    private double total;
    private LocalDate date;
    private String schoolId;
    private String standard;

}