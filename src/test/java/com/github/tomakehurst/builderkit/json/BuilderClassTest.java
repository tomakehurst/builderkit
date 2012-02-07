package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.test.CustomMatchers.argumentName;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.argumentType;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.named;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.returnType;
import static com.google.common.collect.Iterables.find;
import static net.sf.json.test.JSONAssert.assertJsonEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.google.common.base.Predicate;

public class BuilderClassTest {

	@SuppressWarnings("unchecked")
	@Test
	public void scalarsOnlyObjectAtTopLevel() {
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
		
		assertThat(builderClass.getInnerBuilderClasses().size(), is(0));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void objectContainingObjectAtTopLevel() {
	    BuilderClass builderClass = create("NestedObject",
		        "{                                    \n" + 
		        "    \"innerThing\": {                \n" + 
		        "        \"somekey\": \"Some value\"  \n" + 
		        "    }                                \n" + 
		        "}"
		);
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThing"), returnType("NestedObjectBuilder"),
				        argumentType("InnerThingBuilder"), argumentName("innerThing"))
		));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void objectContainingScalarArrayAtTopLevel() {
	    BuilderClass builderClass = create("ScalarArrayObject",
		        "{                                          \n" + 
				"    \"innerThing\": [ \"a string\" ]       \n" + 
				"}"
		);
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThing"), returnType("ScalarArrayObjectBuilder"),
				        argumentType("String..."), argumentName("innerThing"))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void objectContainingObjectArrayAtTopLevel() {
	    BuilderClass builderClass = create("ObjectArrayObject",
		        "{                                              \n" + 
				"    \"innerThings\": [                          \n" + 
				"        {                                      \n" + 
				"            \"somekey\": \"some value\"        \n" + 
				"        }                                      \n" + 
				"    ]                                          \n" + 
				"}"
		);
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withInnerThings"), returnType("ObjectArrayObjectBuilder"),
				        argumentType("InnerThingBuilder..."), argumentName("innerThings"))
		));
		assertThat(builderClass.isArray(), is(false));
		
		assertThat(builderClass.getInnerBuilderClasses().size(), is(1));
		WithMethod firstWithMethodInInnerBuilder = builderClass.getInnerBuilderClasses().get(0).getWithMethods().get(0);
		assertThat(firstWithMethodInInnerBuilder.getSinglularArgumentType(), is("String"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void scalarArrayAtTopLevel() {
	    BuilderClass builderClass = create("Booleans", "[ true ]");
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withItems"), returnType("BooleansBuilder"),
				        argumentType("Boolean..."), argumentName("items"))
		));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void objectArrayAtTopLevel() {
	    BuilderClass builderClass = create("Flavours",
		        "[                                \n" + 
		        "    { \"flavour\": \"Peach\" }   \n" + 
		        "]"
		);
		
		assertThat(builderClass.getWithMethods(), hasItems(
				allOf(named("withItems"), returnType("FlavoursBuilder"),
				        argumentType("ItemBuilder..."), argumentName("items"))
		));
		assertThat(builderClass.isArray(), is(true));
		
		assertThat(builderClass.getInnerBuilderClasses().size(), is(1));
		assertThat(builderClass.getWithMethods().get(0).getSinglularArgumentType(), is("ItemBuilder"));
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
				allOf(named("withSomeKey"), returnType("InnerThingBuilder"),
				        argumentType("String"), argumentName("someKey"))
		));
		assertThat(innerBuilder.isArray(), is(false));
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
	
	@Test
	public void returnsCorrectWithMethodTypes() {
	    BuilderClass builderClass = create("ABitOfEverything",
                "{                                          \n" + 
                "    \"singleNumber\": 5,                   \n" + 
                "    \"booleans\": [ true, false ],      \n" + 
                "    \"singleObject\": {                    \n" + 
                "        \"thing\": 55.6                    \n" + 
                "    },                                     \n" + 
                "    \"objects\": [                     \n" + 
                "        { \"key\": \"Value\" }             \n" + 
                "    ]                                      \n" + 
                "}"
        );
	    
	    List<WithMethod> withMethods = builderClass.getWithMethods();
	    
	    assertThat(find(withMethods, withName("withSingleNumber")).isSingleScalar(), is(true));
	    assertThat(find(withMethods, withName("withBooleans")).isScalarArray(), is(true));
	    assertThat(find(withMethods, withName("withSingleObject")).isSingleObject(), is(true));
	    assertThat(find(withMethods, withName("withObjects")).isObjectArray(), is(true));
	}
	
	private Predicate<WithMethod> withName(final String name) {
	    return new Predicate<WithMethod>() {
            public boolean apply(WithMethod input) {
                return input.getName().equals(name);
            }
	    };
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
