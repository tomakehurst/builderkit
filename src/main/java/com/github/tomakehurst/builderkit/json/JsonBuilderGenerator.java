package com.github.tomakehurst.builderkit.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.parser.ParseException;
import org.stringtemplate.v4.ST;

import com.google.common.io.CharStreams;

public class JsonBuilderGenerator {
	
	private final String packageName;
	private final String className;
	private final String templateContent;

	public JsonBuilderGenerator(String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
		templateContent = loadStringTemplateContent();
	}

	private String loadStringTemplateContent() {
		InputStream in = getClass().getResourceAsStream("/builder.st");
		try {
			return CharStreams.toString(new InputStreamReader(in));
		} catch (IOException ioe) {
			throw new Defect(ioe);
		}
	}

	public String generate(String json) throws ParseException {
		JsonBuilderModel model = new JsonBuilderModel(json);
		
		ST template = new ST(templateContent, '$', '$')
			.add("package", packageName)
			.add("className", className)
			.add("isArray", false)
			.add("properties", model.getProperties());
		
		return template.render();
	}
}
