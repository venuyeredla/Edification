package org.edification.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class GWord {
    private String word;
    private String phonetic;
    private String origin;
    private Map<String,Object> meaning;
}