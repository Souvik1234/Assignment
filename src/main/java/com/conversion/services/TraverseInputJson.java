package com.conversion.services;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class TraverseInputJson {
    JsonNode rootNode;
    ObjectMapper mapper = new ObjectMapper();
    Map<String,Object> output_map = new HashMap<>();

    public Map<String,Object> traverserInput(JsonNode input){
        Map<String,Object> rootMap = mapper.convertValue(input,Map.class);

        for(String key: rootMap.keySet()){

            if(rootMap.get(key) instanceof Map){
               // rootNode = input.path(key);
                traverserInput(mapper.convertValue((rootMap.get(key)),JsonNode.class));

            }
            if(rootMap.get(key) instanceof ArrayList)
            {
                ArrayList<Object> list = mapper.convertValue(rootMap.get(key),ArrayList.class);

                for(Object obj : list){

                    if(obj instanceof Map){

                        traverserInput(mapper.convertValue(obj,JsonNode.class));
                    }
                    if(obj instanceof String || obj instanceof Integer){
                    	output_map.put(key, obj);
                        System.out.println(key+"-->"+obj);
                    }
                }

            }
            if(rootMap.get(key) instanceof String || rootMap.get(key) instanceof Integer){
            	output_map.put(key, rootMap.get(key));
                System.out.println(key+"-->"+rootMap.get(key));
            }
        }
        return output_map;
    }
    
}
