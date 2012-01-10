package com.test.something;

import static java.lang.System.out;

import org.junit.Test;

public class GeneratedBuildersTest {

	@Test
	public void thingBuilder() {
		out.println(new ThingBuilder()
			.withName("Tom")
			.withAddress(new ThingBuilder.AddressBuilder()
			    .withCity("York")
			    .withOptionSet(new ThingBuilder.AddressBuilder.OptionSetBuilder()
			        .withMarketingOptIn(true)))
			.asJson());
	}
	
	@Test
	public void listyThingBuilder() {
	    out.println(new ListyThingBuilder().asJson());
	}
	
	@Test
	public void objectyListyThingBuilder() {
	    out.println(new ObjectyListyThingBuilder().asJson());
	    
	    out.println(new ObjectyListyThingBuilder()
	        .withName("Listy")
	        .withThings(new ObjectyListyThingBuilder.ThingBuilder()
	            .withDescription("listy 1"),
	            new ObjectyListyThingBuilder.ThingBuilder()
                .withDescription("listy 2"),
                new ObjectyListyThingBuilder.ThingBuilder()
                .withDescription("listy 3"))
            .asJson());
                
	}
}
