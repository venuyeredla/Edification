package org.edification.controllers;

import org.edification.models.Clause;
import org.edification.models.DictWord;
import org.edification.service.ClausesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/clauses")
public class ClausesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClausesController.class);

    @Autowired
    ClausesService clausesService;

    @GetMapping("index")
    public boolean indexClauses() {
        this.clausesService.indexClauses();
        return true;
    }

    @GetMapping("import")
    public Map<String, String> importToDB(){
        List<Clause> clauses = this.clausesService.loadFromFile();
        Map<String,String> result=new HashMap<>();
        result.put("imported",clauses.size()+"");
        return result;
    }

    @GetMapping("export")
    public void exportToFile(){
        this.clausesService.writeClausesToFile();
    }

    @GetMapping("define/{word}")
    public List<Clause> getDefintions(@PathVariable(name = "word",required = true) String query){
        LOGGER.info("Query is = {}",query);
        return this.clausesService.getClauses(query);
    }

}
