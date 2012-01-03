package com.github.tomakehurst.builderkit.json;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JsonBuilderModelTest {

	@Test
	public void supportsStringProperties() throws Exception {
		String json =
			"{ \"name\": \"Thomas\", \"favouriteColour\": \"Black like the night\" }";
		
		JsonBuilderModel model = new JsonBuilderModel(json);
		
		assertThat(model.getProperties(), hasItems(
				new Property(Property.Type.STRING, "name", "Thomas"),
				new Property(Property.Type.STRING, "favouriteColour", "Black like the night")));
		assertThat(model.getProperties().size(), is(2));
	}
	
	@Test
	public void supportsLongProperties() throws Exception {
		String json =
			"{ \"age\": 32, \"meaningOfLife\": 42 }";
		
		JsonBuilderModel model = new JsonBuilderModel(json);
		
		assertThat(model.getProperties(), hasItems(
				new Property(Property.Type.LONG, "age", 32),
				new Property(Property.Type.LONG, "meaningOfLife", 42)));
	}
	
	@Test
	public void supportsDoubleProperties() throws Exception {
		String json = "{ \"width\": 51.1234 }";
		
		JsonBuilderModel model = new JsonBuilderModel(json);
		
		assertThat(model.getProperties(), hasItems(
				new Property(Property.Type.DOUBLE, "width", 51.1234)));
	}
	
	@Test
	public void supportsBooleanProperties() throws Exception {
		String json = "{ \"truth\": true, \"falsehood\": false }";
		
		JsonBuilderModel model = new JsonBuilderModel(json);
		
		assertThat(model.getProperties(), hasItems(
				new Property(Property.Type.BOOLEAN, "truth", true),
				new Property(Property.Type.BOOLEAN, "falsehood", false)));
	}
	
	@Test
	public void returnsNoPackageJavaTypeName() throws Exception {
		String json = "{ \"width\": 51.1234 }";
		
		JsonBuilderModel model = new JsonBuilderModel(json);
		
		assertThat(model.getProperties().get(0).getType().getJavaClassNoPackage(), is("Double"));
	}
	
	@Test
	public void correclyGeneratesUppercaseFirstLetterPropertyName() throws Exception {
		String json = "{ \"width\": 51.1234 }";
		
		JsonBuilderModel model = new JsonBuilderModel(json);
		
		assertThat(model.getProperties().get(0).getNameFirstLetterUppercase(), is("Width"));
	}
}
