package com.conversion.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TraverseMappingFile {
    
    ObjectMapper mapper = new ObjectMapper();
    Map<List<String>, List<Object>> output_map = new HashMap<>();
    List<String> finalKey=new ArrayList<>();
    List<Object> finalValue=new ArrayList<>();
   private  JsonNode root ;
   
 
    public  void traverserInput(JsonNode input){
        Map<String,Object> rootMap = mapper.convertValue(input,Map.class);

        for(String key: rootMap.keySet()){

            if(rootMap.get(key) instanceof Map){
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
                        finalKey.add(key);
                        finalValue.add(obj);
                    }
                }

            }
            if(rootMap.get(key) instanceof String || rootMap.get(key) instanceof Integer){

                finalKey.add(key);
                finalValue.add(rootMap.get(key));
            }
        }


    }
    
    
    

    public List<String> getListOfKey(){
        return finalKey;
    }

    public List<Object> getListOfValue(){
        return finalValue;
    }


}
