package com.test.something;

import org.junit.Test;

public class ThingBuilderTest {

	@Test
	public void outputJson() {
		System.out.println(new ThingBuilder()
			.withName("Tom")
			.withFavouriteColour("Red")
			.withLikesCheese(true)
			.withNumberOfNoveltyMugs(5l)
			.withPercentSatisfied(22.2)
			.asJson());
	}
}
