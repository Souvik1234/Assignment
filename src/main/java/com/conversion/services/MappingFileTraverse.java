package com.conversion.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MappingFileTraverse {

    @Autowired
    private TraverseMappingFile traverseInputJson;

    private ObjectMapper mapper = new ObjectMapper();
    public List<Object> getMappingFile()throws Exception{
        Map<String,Object> value = new HashMap<>();
        traverseInputJson.traverserInput
                (mapper.readTree(new File("MappingFile.json")));
        System.out.println("**************************************************");

        List<String> allKeys = traverseInputJson.getListOfKey();
        List<Object> allValues = traverseInputJson.getListOfValue();



        return allValues;
    }
}
