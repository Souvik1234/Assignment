package com.conversion.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

    List<Object> inside = new ArrayList<>();
    List<String> output_value = new ArrayList<>();
    List<String> output_key = new ArrayList<>();
    Map<Object,Object> output_total = new HashMap<>();
    
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> input_map;
    public Map<Object,Object> printOutPutJson(JsonNode input,Map<String, Object> input_map, List<Object> mappingFile){
    	this.input_map = input_map;
        System.out.println("*****************************************************************************");
            for (String inputKey: input_map.keySet()) {

               for(Object o: mappingFile){
                   if(o.equals(inputKey)) {
                       Object s = getMatchedValue(input_map, inputKey);
                       System.out.println(inputKey + "--->" + s);
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
    	Map<String,Object> innermap = new HashMap<>();
    	root = mapper.readTree(new File("MappingFile.json"));
    	
    	mainLocal = mapper.convertValue(root, Map.class);

    	local =root.path("event").path("commonEventHeader");
    	local2=root.path("event").path("faultFields");
    	local3=root.path("event").path("faultFields").path("alarmAdditionalInformation");
    	
    	map = mapper.convertValue(local,Map.class);
    	map2 = mapper.convertValue(local2,Map.class);
    	List<Map<String,Object>> innerList = mapper.convertValue(local3, List.class);
    	map3 = mapper.convertValue(local2,Map.class);
    	//System.out.println(map);
    	for(String keys: map.keySet()) {
    		Object matched =getMatchedValue(input_map, map.get(keys).toString());
    		if(matched != null)
    		map.put(keys, matched);
    		
    	}
    	System.out.println(map);
    	
    	
    	int index=0 ;
    	for(Map<String,Object> in : innerList) {
    		Object val = getMatchedValue(input_map, in.get("name").toString());
    		
    		in.put("value", val);
    		
    		
    		innerList.set(index, in);
    		index++;
    	}
    	
    	
    	/*for(String keys: map3.keySet()) {
    		
    		
    		if(!(map3.get(keys) instanceof ArrayList)) {
    		innermap = mapper.convertValue(map3.get(keys), Map.class);
    		
    		Object matched =getMatchedValue(innermap, keys);
    		if(matched != null)
    		map.put(keys, matched);
    		}
    		//System.out.println(map);
    	}*/
    	
    	map3.put("alarmAdditionalInformation",innerList );
    	
    	insideLocal.put("commonEventHeader", map);
    	insideLocal.put("faultFields", map3);
    	mainLocal.put("event", insideLocal);
    	

    	return mainLocal;
    }
    

    private Object getMatchedValue(Map<String, Object> input_map,String inputKey){
        Object ret =null;
        for(String keys: input_map.keySet()){
            if(keys.equals(inputKey))
                ret = input_map.get(keys);
        }
        return  ret;

    }


}