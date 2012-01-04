package com.test.something;

import org.junit.Test;

public class ThingBuilderTest {

	@Test
	public void outputJson() {
		System.out.println(new ThingBuilder()
			.withName("Tom")
			.withAddress(new ThingBuilder.AddressBuilder()
			    .withCity("York")
			    .withFlags(new ThingBuilder.AddressBuilder.FlagsBuilder()
			        .withMarketingOptIn(true)))
			.asJson());
	}
}
