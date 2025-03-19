
package com.appleUniform.au.bill.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "styles")
@Getter
@Setter
public class Style {
    @Id
    private String styleId;
    private String name;
    private String pattern;
    private Map<String, Double> departmentRates;
}
