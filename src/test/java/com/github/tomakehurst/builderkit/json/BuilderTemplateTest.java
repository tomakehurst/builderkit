package com.github.tomakehurst.builderkit.json;

import static com.google.common.collect.Maps.newHashMap;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.junit.Test;
import org.stringtemplate.v4.ST;

import com.google.common.io.CharStreams;

public class BuilderTemplateTest {

	@Test
	public void populatesBasics() throws Exception {
		InputStream in = getClass().getResourceAsStream("/builder.st");
		String templateContent = CharStreams.toString(new InputStreamReader(in));
		
		Map<String, Property> props = newHashMap();
		props.put("one", new Property(Property.Type.STRING, "one", "onevalue"));
		props.put("two", new Property(Property.Type.BOOLEAN, "two", false));
		props.put("three", new Property(Property.Type.LONG, "three", 333));
		props.put("four", new Property(Property.Type.DOUBLE, "four", 44.44));
		
		ST template = new ST(templateContent, '$', '$')
			.add("package", "com.test.something")
			.add("className", "ThingBuilder")
			.add("isArray", false)
			.add("properties", props.entrySet());
		
		System.out.println(template.render());
	}
	
}
