package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.test.CustomMatchers.argumentType;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.named;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.returnType;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

public class BuilderClassTest {

	@SuppressWarnings("unchecked")
	@Test
	public void returnsScalarWithMethods() {
		JSONObject obj = new JSONObject();
		obj.put("name", "Tom");
		obj.put("age", 111L);
		obj.put("married", true);
		obj.put("height", 5.6);
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(obj);
		BuilderClass builderClass = BuilderClass.fromRootAttribute(model.getRootAttribute(), "BasicObject");
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withName"), returnType("BasicObjectBuilder"), argumentType("String")),
				allOf(named("withAge"), returnType("BasicObjectBuilder"), argumentType("Long")),
				allOf(named("withMarried"), returnType("BasicObjectBuilder"), argumentType("Boolean")),
				allOf(named("withHeight"), returnType("BasicObjectBuilder"), argumentType("Double"))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsObjectWithMethod() {
		JSONObject obj = new JSONObject();
		JSONObject inner = new JSONObject();
		obj.put("innerThing", inner);
		inner.put("somekey", "Some value");
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(obj);
		BuilderClass builderClass = BuilderClass.fromRootAttribute(model.getRootAttribute(), "NestedObject");
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThing"), returnType("NestedObjectBuilder"), argumentType("InnerThingBuilder"))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsScalarArrayWithMethod() {
		JSONObject obj = new JSONObject();
		JSONArray inner = new JSONArray();
		obj.put("innerThing", inner);
		inner.add("a string");
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(obj);
		BuilderClass builderClass = BuilderClass.fromRootAttribute(model.getRootAttribute(), "ScalarArrayObject");
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThing"), returnType("ScalarArrayObjectBuilder"), argumentType("String..."))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsObjectArrayWithMethod() {
		JSONObject obj = new JSONObject();
		JSONArray inner = new JSONArray();
		obj.put("innerThing", inner);
		
		JSONObject arrayItem = new JSONObject();
		arrayItem.put("somekey", "some value");
		inner.add(arrayItem);
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(obj);
		BuilderClass builderClass = BuilderClass.fromRootAttribute(model.getRootAttribute(), "ObjectArrayObject");
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThing"), returnType("ObjectArrayObjectBuilder"), argumentType("InnerThingBuilder..."))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void hasOneScalarArrayWithMethodWhenRootObjectIsScalarArray() {
		JSONArray obj = new JSONArray();
		obj.add(true);
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(obj);
		BuilderClass builderClass = BuilderClass.fromRootAttribute(model.getRootAttribute(), "Booleans");
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withItem"), returnType("BooleansBuilder"), argumentType("Boolean..."))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void hasOneObjectArrayWithMethodWhenRootObjectIsObjectArray() {
		JSONArray obj = new JSONArray();
		JSONObject element = new JSONObject();
		element.put("flavour", "Peach");
		obj.add(element);
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(obj);
		BuilderClass builderClass = BuilderClass.fromRootAttribute(model.getRootAttribute(), "Flavours");
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withItem"), returnType("FlavoursBuilder"), argumentType("ItemBuilder..."))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsInnerBuilderClassForNestedObject() {
		JSONObject obj = new JSONObject();
		JSONObject inner = new JSONObject();
		obj.put("innerThing", inner);
		inner.put("someKey", "Some value");
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(obj);
		BuilderClass builderClass = BuilderClass.fromRootAttribute(model.getRootAttribute(), "NestedObject");
		
		BuilderClass innerBuilder = builderClass.getInnerBuilderClasses().get(0);
		
		assertThat(innerBuilder.getWithMethods(), hasItems(
				allOf(named("withSomeKey"), returnType("InnerThingBuilder"), argumentType("String"))
		));
	}
}
