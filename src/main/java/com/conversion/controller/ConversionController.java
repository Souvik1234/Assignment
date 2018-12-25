package com.conversion.controller;


import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.conversion.services.*;


@RestController
public class ConversionController {


	@Autowired
	private TraverseInputJson traverseInputJson;

	@Autowired
	private TraverseInputJson traverseMappingFileJson;



	@Autowired
	private MappingFileTraverse mappingFileTraverse;

	@Autowired
	private MapToOutput output;

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	TraverseMappingFile t;
	@RequestMapping("/convert")
	public  Map<String, Object> doConvert(@RequestBody JsonNode input) throws Exception {

		Map<String,Object> input_map = traverseInputJson.traverserInput(input);
		List<Object> mappingFile =mappingFileTraverse.getMappingFile();
		output.printOutPutJson(input,input_map,mappingFile);

		t.traverserInput(mapper.readTree(new File("MappingFile.json")));



		return output.displayFinal(); //calling to print o/p


	}


}
