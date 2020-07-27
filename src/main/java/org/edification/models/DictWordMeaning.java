package org.edification.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DictWordMeaning {
      private String pos;
      List<DictWordDef> definitions;
}
