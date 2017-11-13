package com.cyborg.sportparser.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Parameter {

    private String name;
    private String value;


    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
