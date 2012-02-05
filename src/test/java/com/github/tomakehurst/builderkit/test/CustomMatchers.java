package com.github.tomakehurst.builderkit.test;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.github.tomakehurst.builderkit.json.Attribute;
import com.github.tomakehurst.builderkit.json.BuilderClass;
import com.github.tomakehurst.builderkit.json.BuilderClass.WithMethod;

public class CustomMatchers {

	public static <T extends Attribute> Matcher<T> withTypeAndName(final Attribute.Type type, final String name) {
		return new TypeSafeMatcher<T>() {
			public void describeTo(Description desc) {
				desc.appendText(String.format("Attribute of type %s named '%s'", type, name));
			}

			public boolean matchesSafely(T attribute) {
				return attribute.getName().toString().equals(name);
			}
			
		};
	}

	public static Matcher<BuilderClass.WithMethod> named(final String name) {
		return new TypeSafeMatcher<BuilderClass.WithMethod>() {
			public void describeTo(Description desc) {
				desc.appendText("with method named " + name);
				
			}

			public boolean matchesSafely(WithMethod withMethod) {
				return withMethod.getName().equals(name);
			}
			
		};
	}
	
	public static Matcher<BuilderClass.WithMethod> returnType(final String returnType) {
		return new TypeSafeMatcher<BuilderClass.WithMethod>() {
			public void describeTo(Description desc) {
				desc.appendText("with method with return type " + returnType);
				
			}

			public boolean matchesSafely(WithMethod withMethod) {
				return withMethod.getReturnType().equals(returnType);
			}
			
		};
	}
	
	public static Matcher<BuilderClass.WithMethod> argumentType(final String argumentType) {
		return new TypeSafeMatcher<BuilderClass.WithMethod>() {
			public void describeTo(Description desc) {
				desc.appendText("with argument type " + argumentType);
				
			}

			public boolean matchesSafely(WithMethod withMethod) {
				return withMethod.getArgumentType().equals(argumentType);
			}
			
		};
	}
}
