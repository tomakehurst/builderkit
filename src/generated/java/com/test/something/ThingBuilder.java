package com.test.something;

import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

public class ThingBuilder {

	private Map<String, Object> rootObject;

	@SuppressWarnings("unchecked")	
	public ThingBuilder() {
		rootObject = new JSONObject();
	}

	public ThingBuilder withFavouriteColour(String favouriteColour) {
		rootObject.put("favouriteColour", favouriteColour);
		return this;
	}

	public ThingBuilder withPercentSatisfied(Double percentSatisfied) {
		rootObject.put("percentSatisfied", percentSatisfied);
		return this;
	}

	public ThingBuilder withNumberOfNoveltyMugs(Long numberOfNoveltyMugs) {
		rootObject.put("numberOfNoveltyMugs", numberOfNoveltyMugs);
		return this;
	}

	public ThingBuilder withName(String name) {
		rootObject.put("name", name);
		return this;
	}

	public ThingBuilder withLikesCheese(Boolean likesCheese) {
		rootObject.put("likesCheese", likesCheese);
		return this;
	}



	public String asJson() {
		return rootObject.toString();
	}

}