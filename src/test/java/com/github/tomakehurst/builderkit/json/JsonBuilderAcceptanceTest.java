package com.github.tomakehurst.builderkit.json;

import static java.lang.System.out;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;

public class JsonBuilderAcceptanceTest {

	@Test
	public void generatesBuilderForSingleLevelObject() throws Exception {
		String json =
			"{ 													\n" +
			"	\"name\": \"Thomas\", 							\n" +
			"	\"favouriteColour\": \"Black like the night\", 	\n" +
			"	\"likesCheese\": true,	 						\n" +
			"	\"numberOfNoveltyMugs\": 12,				 	\n" +
			"	\"percentSatisfied\": 76.234				 	\n" +
			"}";
		
		JsonBuilderGenerator generator = new JsonBuilderGenerator("com.test.something", "Thing", json);

		generator.writeToFileUnder("src/generated/java");
		assertTrue(new File("src/generated/java/com/test/something/ThingBuilder.java").exists());
		
		String builderJava = generator.generate();
		out.println(builderJava);
		
		assertThat(builderJava, containsString("public ThingBuilder withName(String name)"));
		assertThat(builderJava, containsString("public ThingBuilder withFavouriteColour(String favouriteColour)"));
		assertThat(builderJava, containsString("public ThingBuilder withLikesCheese(Boolean likesCheese)"));
		assertThat(builderJava, containsString("public ThingBuilder withNumberOfNoveltyMugs(Long numberOfNoveltyMugs)"));
		assertThat(builderJava, containsString("public ThingBuilder withPercentSatisfied(Double percentSatisfied)"));
	}
	
	@Test
	public void generatesInnerClassesForNestedObjects() throws Exception {
		String json =
			"{ 													\n" +
			"	\"name\": \"Thomas\", 							\n" +
			"	\"address\": { 									\n" +
			"		\"houseNumber\": 12,	 					\n" +
			"		\"street\": \"Wheat Street\",				\n" +
			"		\"city\": \"Trumpton\",						\n" +
			"       \"optionSet\": {                            \n" +
			"            \"marketingOptIn\": true,              \n" +
			"            \"shareWithPartners\": false           \n" +
			"       }                                           \n" +
			"	}											 	\n" +
			"}";
		
		JsonBuilderGenerator generator = new JsonBuilderGenerator("com.test.something", "Thing", json);
		String builderJava = generator.generate();
		out.println(builderJava);
		generator.writeToFileUnder("src/generated/java");
		
		assertThat(builderJava, containsString("public static class AddressBuilder {"));
		assertThat(builderJava, containsString("public AddressBuilder withHouseNumber(Long houseNumber)"));
		assertThat(builderJava, containsString("public AddressBuilder withStreet(String street)"));
		assertThat(builderJava, containsString("public AddressBuilder withCity(String city)"));
		
		assertThat(builderJava, containsString("public OptionSetBuilder withMarketingOptIn(Boolean marketingOptIn)"));
		assertThat(builderJava, containsString("public OptionSetBuilder withShareWithPartners(Boolean shareWithPartners)"));
	}
	
	@Test
    public void scalarListAtTopLevel() throws Exception {
	    String json = "[ 1, 2, 3, 4 ]";
	    
	    JsonBuilderGenerator generator = new JsonBuilderGenerator("com.test.something", "ListyThing", json);
        String builderJava = generator.generate();
        out.println(builderJava);
        generator.writeToFileUnder("src/generated/java");
	}
	
	@Test
	public void objectListAtTopLevel() throws Exception {
	    String json =
            "[                                                  \n" +
            "       {                                           \n" +
            "           \"description\": \"dull\"               \n" +
            "       },                                          \n" +
            "       {                                           \n" +
            "           \"description\": \"bright\"             \n" +
            "       }                                           \n" +
            "]                                                  ";
        
        JsonBuilderGenerator generator = new JsonBuilderGenerator("com.test.something", "TopLevelListOfObjects", json);
        String builderJava = generator.generate();
        out.println(builderJava);
        generator.writeToFileUnder("src/generated/java");
	}
	
	@Test
	public void generatesBuilderForMixedObjectsAndArrays() throws Exception {
	    String json =
            "{                                                  \n" +
            "   \"name\": \"Thomas\",                           \n" +
            "   \"things\": [                                   \n" +
            "       {                                           \n" +
            "           \"description\": \"dull\"               \n" +
            "       },                                          \n" +
            "       {                                           \n" +
            "           \"description\": \"bright\"             \n" +
            "       }                                           \n" +
            "   ]                                               \n" +
            "}";
	    
	    JsonBuilderGenerator generator = new JsonBuilderGenerator("com.test.something", "ObjectyListyThings", json);
        String builderJava = generator.generate();
        out.println(builderJava);
        generator.writeToFileUnder("src/generated/java");
	}
	
	@Test
	public void scalarLists() throws Exception {
		String json =
            "{                                                  \n" +
            "   \"numbers\": [ 1, 2, 3 ],                       \n" +
            "   \"strings\": [ \"one\", \"two\", \"three\" ],   \n" +
            "   \"booleans\": [ true, false, true ]             \n" +
            "}";
	    
	    JsonBuilderGenerator generator = new JsonBuilderGenerator("com.test.something", "ScalarLists", json);
        String builderJava = generator.generate();
        out.println(builderJava);
        generator.writeToFileUnder("src/generated/java");
	}
}
