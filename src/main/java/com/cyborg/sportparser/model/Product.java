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

    private String brand;
    private String image;


    public Product(String name, Description description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
