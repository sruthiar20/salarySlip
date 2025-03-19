
package com.appleUniform.au.bill.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "workers")
@Getter
@Setter
public class Worker {
    @Id
    private String workerId;
    private String name;
    private String department;

}
