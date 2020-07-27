package org.edification.service;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.edification.models.Clause;
import org.edification.models.DictWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Setter
public class ClausesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexerService.class);

    @Autowired
    @Qualifier("clausesRepo")
    ObjectRepository<Clause> clausesRepo;

    @Autowired
    IndexerService indexerService;
    @Autowired
    FileService fileService;
    
    public List<Clause> loadFromFile(){
        List<Clause> clauses = this.fileService.readClausesFromFile();
        clausesRepo.remove(ObjectFilters.ALL);
        clauses.stream().filter(Objects::nonNull).filter(clause -> StringUtils.isNotBlank(clause.getClause())).forEach(clause -> {
            clause.setId(clause.getType()+"-"+clause.getClause());
            try{
                clausesRepo.insert(clause);
            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return clauses;
    }

    public List<Clause> getClauses(String query){
         if(StringUtils.isNotBlank(query)){
            return this.clausesRepo.find(ObjectFilters.eq("id",query)).toList();
         }else{
             return clausesRepo.find().toList();
         }
    }

    public String writeClausesToFile(){
        String filePath = this.fileService.writeClausesTofile(this.getClauses(null));
        return filePath;
    }

    public void indexClauses(){
        this.indexerService.indexToSolr(this.getClauses(null));
    }

}

