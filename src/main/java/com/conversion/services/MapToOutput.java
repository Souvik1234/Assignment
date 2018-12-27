package com.conversion.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MapToOutput {

	@Autowired
	private DisplayTimeInnerMap innerMap;
    List<Object> inside = new ArrayList<>();
    List<String> output_value = new ArrayList<>();
    List<String> output_key = new ArrayList<>();
    Map<Object,Object> output_total = new HashMap<>();
    
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> input_map;
    public Map<Object,Object> printOutPutJson(JsonNode input,Map<String, Object> input_map, List<Object> mappingFile){
    	this.input_map = input_map;
        
            for (String inputKey: input_map.keySet()) {

               for(Object o: mappingFile){
                   if(o.equals(inputKey)) {
                       Object s = getMatchedValue(input_map, inputKey);
                       
                       output_total.put(inputKey, s);
                   }
               }
            }
        return  output_total;
    }
    
    
    public Map<String, Object> displayFinal() throws Exception
    {
    		Map<String,Object> mainLocal =new HashMap();
    		Map<String,Object> insideLocal = new HashMap<>();
    		
    	 JsonNode local;
    	 JsonNode root;
    	 JsonNode local2;
    	 JsonNode local3;
    	Map<String, Object> map= new HashMap<>();
    	Map<String, Object> map2= new HashMap<>();
    	Map<String, Object> map3= new HashMap<>();
    	Map<String, Object> map4= new HashMap<>();
    	Map<String,Object> innermap = new HashMap<>();
    	root = mapper.readTree(new File("MappingFile.json"));
    	
    	mainLocal = mapper.convertValue(root, Map.class); //getting Mapping File

    	local =root.path("event").path("commonEventHeader"); 
    	local2=root.path("event").path("faultFields");
    	local3=root.path("event").path("faultFields").path("alarmAdditionalInformation");
    	
    	map = mapper.convertValue(local,Map.class); //mapped version of commonEventHeader
    	map2 = mapper.convertValue(local2,Map.class); //mapped version of faultFields
    	List<Map<String,Object>> innerList = mapper.convertValue(local3, List.class); // Array of alarmAdditionalInformation
    	
    	map3 = innerMap.breakInnerMappingFile(local2);
    	
    	for(String keys: map.keySet()) {
    		Object matched =getMatchedValue(input_map, map.get(keys).toString());
    		if(matched != null) {
    			
					map.put(keys, matched);
			}
    		if(map.get(keys).toString().contains("put_")) {
    			map.put(keys,map.get(keys).toString().substring(4));
    		}
    		
    		
    		
    	}
    	int index=0 ;
    	for(Map<String,Object> in : innerList) { //assigning values to key of aditionalalarmcondition field
			Object val = getMatchedValue(input_map, in.get("name").toString());
			in.put("value", val);
			
			innerList.set(index, in);
			index++;
			
		}
    	
    	for(Map<String,Object> in : innerList) { //removing "put_" from value
    	for (String keys:in.keySet()) {
			
			String modi=in.get(keys).toString();
			
			if(modi.contains("put_")) {
			modi = modi.substring(4);
			in.put("name", modi);
			
			}
		}
    	}
    	
    	for(String keys: map3.keySet()) {
    		Object matched =getMatchedValue(input_map, map3.get(keys).toString());
    		if(matched != null) {
				
					map4.put(keys, matched);
					
			}
    		if(map3.get(keys).toString().contains("put_")) {
    			
    			for(String keys_in: map2.keySet()) {
    				if(map2.get(keys_in).toString().contains("put_")) {
    					String modi = map2.get(keys_in).toString().substring(4);
    					map2.put(keys_in,modi);	
    				}
    			}
    		}
    	}
    	
    	map2.put("alarmAdditionalInformation",innerList ); 
    	
    	insideLocal.put("faultFields", map4);
    	insideLocal.put("commonEventHeader", map);
    	insideLocal.put("faultFields", map2);
    	mainLocal.put("event", insideLocal);
    	

    	return mainLocal;
    }
    

    private Object getMatchedValue(Map<String, Object> input_map,String inputKey){
        Object ret =null;
        if(inputKey.contains("put_")) {
			inputKey = inputKey.substring(4);

			for (String keys : input_map.keySet()) {
				if (keys.equals(inputKey))
					if (keys.equals("target")) {

						ret = cutTargetCustomized(input_map.get(keys).toString());
					} else {
						ret = input_map.get(keys);
					}
			}
		}
        return  ret;

    }

    private Object cutTargetCustomized(String s){
		String[] f = s.split("/");
		String fo[] = f[1].split("=");
		return fo[1];

	}


}
