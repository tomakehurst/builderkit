package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Attribute.Type.BOOLEAN;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.LONG;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.OBJECT;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.STRING;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.withTypeAndName;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import com.github.tomakehurst.builderkit.json.Attribute.Type;

public class AttributeTest {

	@Test
	public void buildsStringAttribute() {
		Attribute attribute = Attribute.fromJsonAttribute(jsonAttribute("city", "London"));
		assertThat(attribute.getName().toString(), is("city"));
		assertThat(attribute.getType(), is(STRING));
	}
	
	@Test
	public void buildsBooleanAttribute() {
		Attribute attribute = Attribute.fromJsonAttribute(jsonAttribute("likesCheese", true));
		assertThat(attribute.getName().toString(), is("likesCheese"));
		assertThat(attribute.getType(), is(BOOLEAN));
	}
	
	@Test
	public void buildsLongAttribute() {
		Attribute attribute = Attribute.fromJsonAttribute(jsonAttribute("meaningOfLife", 42L));
		assertThat(attribute.getName().toString(), is("meaningOfLife"));
		assertThat(attribute.getType(), is(LONG));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void buildsStringArrayAttribute() {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add("one");
		jsonArray.add("two");
		jsonArray.add("three");
		
		Attribute attribute = Attribute.fromJsonAttribute(jsonAttribute("numbers", jsonArray));
		assertThat(attribute.getName().toString(), is("numbers"));
		assertThat(attribute.getType(), is(Type.ARRAY));
		assertThat(attribute, instanceOf(ArrayAttribute.class));
		
		ArrayAttribute arrayAttribute = (ArrayAttribute) attribute;
		assertThat(arrayAttribute.getElementType(), is(STRING));
		assertThat(arrayAttribute.getDefaultValueJson(), is("[\"one\",\"two\",\"three\"]"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void buildsLongArrayAttribute() {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(5L);
		jsonArray.add(2573L);
		jsonArray.add(666L);
		
		ArrayAttribute arrayAttribute = Attribute.fromJsonAttribute(jsonAttribute("numbers", jsonArray));
		assertThat(arrayAttribute.getElementType(), is(LONG));
		assertThat(arrayAttribute.getDefaultValueJson(), is("[5,2573,666]"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void buildsObjectArrayAttribute() {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(singleElementObject("city", "London"));
		jsonArray.add(singleElementObject("city", "Amsterdam"));
		jsonArray.add(singleElementObject("city", "Paris"));
		
		ArrayAttribute arrayAttribute = Attribute.fromJsonAttribute(jsonAttribute("cities", jsonArray));
		assertThat(arrayAttribute.getElementType(), is(OBJECT));
		assertThat(arrayAttribute.getDefaultValueJson(), is("[{\"city\":\"London\"},{\"city\":\"Amsterdam\"},{\"city\":\"Paris\"}]"));
		
		assertThat(arrayAttribute.getElementAttributes(), hasItems(withTypeAndName(STRING, "city")));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void buildsArrayArrayAttribute() {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(array("1,1", "1,2", "1,3"));
		jsonArray.add(array("2,1", "2,2", "2,3"));
		jsonArray.add(array("3,1", "3,2", "3,3"));
		
		ArrayAttribute arrayAttribute = Attribute.fromJsonAttribute(jsonAttribute("cities", jsonArray));
		assertThat(arrayAttribute.getElementType(), is(Type.ARRAY));
		assertThat(arrayAttribute.getDefaultValueJson(), is("[[\"1,1\",\"1,2\",\"1,3\"],[\"2,1\",\"2,2\",\"2,3\"],[\"3,1\",\"3,2\",\"3,3\"]]"));
	}
	
	@Test
	public void defaultsToStringForEmptyArray() {
		JSONArray jsonArray = new JSONArray();
		ArrayAttribute attribute = Attribute.fromJsonAttribute(jsonAttribute("things", jsonArray));
		assertThat(attribute.getElementType(), is(Type.STRING));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void buildsObjectAttribute() {
		JSONObject object = singleElementObject("lengthInches", 12L);
		
		ObjectAttribute objAttribute = Attribute.fromJsonAttribute(jsonAttribute("size", object));
		
		assertThat(objAttribute.getChildAttributes(), hasItems(withTypeAndName(Type.STRING, "lengthInches")));
		assertThat(objAttribute.getDefaultValueJson(), is("{\"lengthInches\":12}"));
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject singleElementObject(String key, Object value) {
		JSONObject obj = new JSONObject();
		obj.put(key, value);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray array(Object... values) {
		JSONArray array = new JSONArray();
		array.addAll(asList(values));
		return array;
	}
	
	private static Map.Entry<String, ?> jsonAttribute(final String key, final Object value) {
		return new Map.Entry<String, Object>() {

			@Override
			public String getKey() {
				return key;
			}

			@Override
			public Object getValue() {
				return value;
			}

			@Override
			public Object setValue(Object arg0) {
				throw new UnsupportedOperationException();
			}
			
		};
	}
}
