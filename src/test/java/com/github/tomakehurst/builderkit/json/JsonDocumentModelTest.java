package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Attribute.Type.ARRAY;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.BOOLEAN;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.DOUBLE;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.LONG;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.OBJECT;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.STRING;
import static com.github.tomakehurst.builderkit.test.CustomMatchers.withTypeAndName;
import static com.google.common.collect.Iterables.find;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.google.common.base.Predicate;

public class JsonDocumentModelTest {

	@SuppressWarnings("unchecked")
	@Test
	public void createsModelForSingleLevelObject() {
		String json =
			"{ 													\n" +
			"	\"name\": \"Thomas\", 							\n" +
			"	\"favouriteColour\": \"Black like the night\", 	\n" +
			"	\"likesCheese\": true,	 						\n" +
			"	\"numberOfNoveltyMugs\": 12,				 	\n" +
			"	\"percentSatisfied\": 76.234				 	\n" +
			"}";
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(parse(json));
		ObjectAttribute attribute = model.getRootAttribute();
		
		assertThat(attribute.getChildAttributes(), hasItems(
				withTypeAndName(STRING, "name"),
				withTypeAndName(STRING, "favouriteColour"), 
				withTypeAndName(BOOLEAN, "likesCheese"), 
				withTypeAndName(LONG, "numberOfNoveltyMugs"), 
				withTypeAndName(DOUBLE, "percentSatisfied")));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createsModelForMultiLevelObjectWithArrays() {
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
				"		\"luckyNumbers\": [ 1, 2, 3 ],				\n" +
				"		\"residents\": [							\n" + 
				"			{										\n" + 
				"				\"name\": \"Jeff\"					\n" + 
				"			},										\n" + 
				"			{										\n" + 
				"				\"name\": \"Jim\"					\n" + 
				"			},										\n" + 
				"			{										\n" + 
				"				\"name\": \"Jeremy\"				\n" + 
				"			}										\n" + 
				"		]											\n" +
				"	}											 	\n" +
				"}";
		
		JsonDocumentModel model = JsonDocumentModel.createFrom(parse(json));
		ObjectAttribute root = model.getRootAttribute();
		
		assertThat(root.getChildAttributes(), hasItems(
				withTypeAndName(STRING, "name"),
				withTypeAndName(OBJECT, "address")));
		
		ObjectAttribute address = (ObjectAttribute) getFrom(root.getChildAttributes(), withTypeAndName(OBJECT, "address")); 
		assertThat(address.getChildAttributes(), hasItems(
				withTypeAndName(ARRAY, "luckyNumbers"),
				withTypeAndName(ARRAY, "residents")));
		
		ArrayAttribute residents = (ArrayAttribute) getFrom(address.getChildAttributes(), withTypeAndName(ARRAY, "residents"));
		assertThat(residents.getElementType(), is(OBJECT));
		assertThat(residents.getElementAttribute().getChildAttributes(), hasItems(withTypeAndName(STRING, "name")));
		assertThat(residents.getElementAttribute(), withTypeAndName(OBJECT, "resident"));
	}
	
	private <T> T getFrom(Iterable<T> items, final Matcher<T> matcher) {
		return (T) find(items, new Predicate<T>() {
			public boolean apply(T input) {
				return matcher.matches(input);
			}
		});
	}
	
	private Object parse(String json) {
		try {
			return new JSONParser().parse(json);
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}
}
