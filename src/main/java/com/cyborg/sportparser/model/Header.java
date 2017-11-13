package com.cyborg.sportparser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Header {

    private String headerTitle;
    private List<Parameter> headerTable;

    public Header(String headerTitle, List<Parameter> headerTable) {
        this.headerTitle = headerTitle;
        this.headerTable = headerTable;
    }
}
