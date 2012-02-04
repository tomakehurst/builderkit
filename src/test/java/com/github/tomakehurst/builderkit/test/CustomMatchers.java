package com.github.tomakehurst.builderkit.test;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.github.tomakehurst.builderkit.json.Attribute;

public class CustomMatchers {

	public static <T extends Attribute> Matcher<T> withTypeAndName(final Attribute.Type type, final String name) {
		return new TypeSafeMatcher<T>() {

			@Override
			public void describeTo(Description desc) {
				desc.appendText(String.format("Attribute of type %s named '%s'", type, name));
			}

			@Override
			public boolean matchesSafely(T attribute) {
				return attribute.getName().toString().equals(name);
			}
			
		};
	}
}
