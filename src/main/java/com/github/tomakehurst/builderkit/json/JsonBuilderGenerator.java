package com.github.tomakehurst.builderkit.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.parser.ParseException;
import org.stringtemplate.v4.ST;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

public class JsonBuilderGenerator {
	
	private final String packageName;
	private final String className;
	private final String templateContent;
	private final JsonBuilderModel model;

	public JsonBuilderGenerator(String packageName, String className, String sourceJson) throws ParseException {
		this.packageName = packageName;
		this.className = className;
		this.model = new JsonBuilderModel(sourceJson);
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

	public String generate() {
		ST template = new ST(templateContent, '$', '$')
			.add("package", packageName)
			.add("className", className)
			.add("isArray", false)
			.add("properties", model.getProperties());
		
		return template.render();
	}
	
	public void writeToFileUnder(String rootDirectory) throws IOException {
		File packageDir = new File(rootDirectory, packageName.replace('.', '/'));
		packageDir.mkdirs();
		File javaFile = new File(packageDir, className + ".java");
		Files.write(generate(), javaFile, Charsets.UTF_8);
	}
}
