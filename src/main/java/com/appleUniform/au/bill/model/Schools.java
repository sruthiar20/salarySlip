
package com.appleUniform.au.bill.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "schools")
@Getter
@Setter
public class Schools {
    @Id
    private String schoolId;
    private String name;

}
