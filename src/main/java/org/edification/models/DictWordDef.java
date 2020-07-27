package org.edification.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DictWordDef {
   private String definition;
   private String example;
   private List<String> synonyms;
   private List<String> antonyms;
}
