package com.cyborg.sportparser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Description {

    private Header header;
    private String headerOfDescription;
    private List<String> descriptions;
    private Composition composition;


    public Description(Header header, String headerOfDescription, List<String> descriptions, Composition composition) {
        this.header = header;
        this.headerOfDescription = headerOfDescription;
        this.descriptions = descriptions;
        this.composition = composition;
    }
}
