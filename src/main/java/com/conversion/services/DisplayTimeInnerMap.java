package com.conversion.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DisplayTimeInnerMap {

	 private ObjectMapper mapper = new ObjectMapper();
	 private Map<String,Object> output_map = new HashMap<>();
	 private  JsonNode root ;
	   
	 
	    public  Map<String,Object> breakInnerMappingFile(JsonNode input){
	    	
	        Map<String,Object> rootMap = mapper.convertValue(input,Map.class);

	        for(String key: rootMap.keySet()){
	        	
	        	
	            if(rootMap.get(key) instanceof String || rootMap.get(key) instanceof Integer || rootMap.get(key) instanceof Double){

	               output_map.put(key, rootMap.get(key));
	            }
	           
	        
	        
	        }
	        return output_map;
	    }
}