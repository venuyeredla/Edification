package org.edification.controllers;

import org.edification.service.ReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class ReaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderController.class);

    @Autowired
    ReaderService readerService;

    @GetMapping("read")
    public List<String> getSentences(){
        return  readerService.getSentences().stream().collect(Collectors.toList());
    }

    @PostMapping("dirFiles")
    private List<String> getFilesList(@RequestBody Map<String,String> input){
    	LOGGER.info("Listing directory files");
        String directoryPath=input.get("directoryPath");
        try (Stream<Path> stream = Files.walk(Paths.get(directoryPath), Integer.MAX_VALUE)) {
            List<String> filesList = stream.map(String::valueOf).filter(name-> name.endsWith(".txt")).sorted().collect(Collectors.toList());
            filesList.forEach(fileName-> {
            });
            return filesList;
        } catch (IOException e) {
                e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
