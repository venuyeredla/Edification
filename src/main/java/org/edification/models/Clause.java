package org.edification.models;

import lombok.Getter;
import lombok.Setter;
import org.dizitart.no2.objects.Id;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Clause {
    @Id
    private String id;
    private String clause;
    private List<String> meanings;
    private List<String> examples;
    private String type;
    private boolean known;
    private int importance;
    private Date lastReadTime;
}
