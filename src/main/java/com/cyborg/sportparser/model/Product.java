package com.cyborg.sportparser.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
public class Product {

    @Id
    @Indexed
    private String id;
    private String name;
    private Description description;


    public Product(String name, Description description) {
        this.name = name;
        this.description = description;
    }
}
