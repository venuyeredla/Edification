package org.edification.models;

import lombok.Getter;
import lombok.Setter;
import org.dizitart.no2.objects.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class DictWord {
    @Id
    private String id;
    private String word;
    private String origin;
    List<DictWordMeaning> meanings=new ArrayList<>();
    private boolean known;
    private boolean full;
    private int importance;
    private Date lastReadTime;
}
