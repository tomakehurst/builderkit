package com.github.tomakehurst.builderkit.json;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonBuilderModel {
	
	private List<Property> properties = newArrayList(); 

	@SuppressWarnings("unchecked")
	public JsonBuilderModel(String jsonString) throws ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(jsonString);
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			Set<Map.Entry<String, ?>> attributes = jsonObject.entrySet();
			for (Map.Entry<String, ?> attribute: attributes) {
				properties.add(Property.fromJsonAttribute(attribute));
			}
		} else if (obj instanceof JSONArray) {
			
		} else {
			throw new RuntimeException("Supplied JSON is not an object or array");
		}
	}

	public List<Property> getProperties() {
		return properties;
	}
	
	
}
