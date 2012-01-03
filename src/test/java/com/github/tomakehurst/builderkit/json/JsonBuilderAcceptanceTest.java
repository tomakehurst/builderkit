package com.github.tomakehurst.builderkit.json;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

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
		
		JsonBuilderGenerator generator = new JsonBuilderGenerator("com.test.something", "ThingBuilder");
		String builderJava = generator.generate(json);
		
		System.out.println(builderJava);
		
		assertThat(builderJava, containsString("public ThingBuilder withName(String name)"));
		assertThat(builderJava, containsString("public ThingBuilder withFavouriteColour(String favouriteColour)"));
		assertThat(builderJava, containsString("public ThingBuilder withLikesCheese(Boolean likesCheese)"));
		assertThat(builderJava, containsString("public ThingBuilder withNumberOfNoveltyMugs(Long numberOfNoveltyMugs)"));
		assertThat(builderJava, containsString("public ThingBuilder withPercentSatisfied(Double percentSatisfied)"));
	}
}
