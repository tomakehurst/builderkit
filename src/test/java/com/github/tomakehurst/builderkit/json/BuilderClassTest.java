package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.test.CustomMatchers.argumentType;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.named;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.returnType;
import static net.sf.json.test.JSONAssert.assertJsonEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class BuilderClassTest {

	@SuppressWarnings("unchecked")
	@Test
	public void returnsScalarWithMethods() {
	    BuilderClass builderClass = create("BasicObject",
	            "{                         \n" + 
	            "    \"name\": \"Tom\",    \n" + 
	            "    \"age\": 111,         \n" + 
	            "    \"married\": true,    \n" + 
	            "    \"height\": 5.6       \n" + 
	            "}"        
	    );
	    
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
	    BuilderClass builderClass = create("NestedObject",
		        "{                                    \n" + 
		        "    \"innerThing\": {                \n" + 
		        "        \"somekey\": \"Some value\"  \n" + 
		        "    }                                \n" + 
		        "}"
		);
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThing"), returnType("NestedObjectBuilder"), argumentType("InnerThingBuilder"))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsScalarArrayWithMethod() {
	    BuilderClass builderClass = create("ScalarArrayObject",
		        "{                                          \n" + 
				"    \"innerThing\": [ \"a string\" ]       \n" + 
				"}"
		);
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThing"), returnType("ScalarArrayObjectBuilder"), argumentType("String..."))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsObjectArrayWithMethod() {
	    BuilderClass builderClass = create("ObjectArrayObject",
		        "{                                              \n" + 
				"    \"innerThing\": [                          \n" + 
				"        {                                      \n" + 
				"            \"somekey\": \"some value\"        \n" + 
				"        }                                      \n" + 
				"    ]                                          \n" + 
				"}"
		);
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThing"), returnType("ObjectArrayObjectBuilder"), argumentType("InnerThingBuilder..."))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void hasOneScalarArrayWithMethodWhenRootObjectIsScalarArray() {
	    BuilderClass builderClass = create("Booleans", "[ true ]");
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withItem"), returnType("BooleansBuilder"), argumentType("Boolean..."))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void hasOneObjectArrayWithMethodWhenRootObjectIsObjectArray() {
	    BuilderClass builderClass = create("Flavours",
		        "[                                \n" + 
		        "    { \"flavour\": \"Peach\" }   \n" + 
		        "]"
		);
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withItem"), returnType("FlavoursBuilder"), argumentType("ItemBuilder..."))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsInnerBuilderClassForNestedObject() {
	    BuilderClass builderClass = create("NestedObject",
		        "{                                        \n" + 
		        "    \"innerThing\": {                    \n" + 
		        "        \"someKey\": \"Some value\"      \n" + 
		        "    }                                    \n" + 
		        "}"
		);
		
		BuilderClass innerBuilder = builderClass.getInnerBuilderClasses().get(0);
		assertThat(innerBuilder.getWithMethods(), hasItems(
				allOf(named("withSomeKey"), returnType("InnerThingBuilder"), argumentType("String"))
		));
	}
	
	@Test
	public void returnsStaticFactoryMethodOnInnerBuilderClass() {
	    BuilderClass builderClass = create("NestedObject",
                "{                                        \n" + 
                "    \"innerThing\": {                    \n" + 
                "        \"someKey\": \"Some value\"      \n" + 
                "    }                                    \n" + 
                "}"
        );
	    
	    BuilderClass innerBuilder = builderClass.getInnerBuilderClasses().get(0);
	    assertThat(innerBuilder.getStaticFactoryMethodName(), is("anInnerThing"));
	    assertThat(innerBuilder.getStaticFactoryReturnType(), is("InnerThingBuilder"));
	}
	
	@Test
	public void returnsDefaultJson() {
	    String json =  
	        "{                                        \n" + 
            "    \"innerThing\": {                    \n" + 
            "        \"someKey\": \"Some value\"      \n" + 
            "    }                                    \n" + 
            "}";
	    BuilderClass builderClass = create("NestedObject", json);
	    
	    assertJsonEquals(json, builderClass.getDefaultJson());
	    assertJsonEquals("{\"someKey\": \"Some value\"}", 
	            builderClass.getInnerBuilderClasses().get(0).getDefaultJson());
	}
	
	private BuilderClass create(String name, String json) {
	    Object obj = parse(json);
	    JsonDocumentModel model = JsonDocumentModel.createFrom(obj);
        BuilderClass builderClass = BuilderClass.fromRootAttribute(model.getRootAttribute(), name, true);
        return builderClass;
	}
	
	@SuppressWarnings("unchecked")
    private <T> T parse(String json) {
	    try {
	        return (T) new JSONParser().parse(json);
	    } catch (ParseException e) {
	        throw new RuntimeException(e);
	    }
	}
}
