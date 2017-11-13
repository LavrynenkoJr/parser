package com.cyborg.sportparser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Composition {

    private String name;
    private List<Parameter> composition;

    public Composition(String name, List<Parameter> composition) {
        this.name = name;
        this.composition = composition;
    }
}
