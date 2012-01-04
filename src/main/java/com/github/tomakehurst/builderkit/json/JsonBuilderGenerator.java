package com.github.tomakehurst.builderkit.json;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class JsonBuilderGenerator {
	
	private final String packageName;
	private final STGroup templateGroup;
	private final ObjectBuilderModel model;

	public JsonBuilderGenerator(String packageName, String entityName, String sourceJson) throws ParseException {
		this.packageName = packageName;
		this.model = topLevelModel(entityName, sourceJson);
		templateGroup = loadStringTemplateGroup();
	}
	
	private ObjectBuilderModel topLevelModel(String entityName, String sourceJson) throws ParseException {
	    JSONParser parser = new JSONParser();
        Object obj = parser.parse(sourceJson);
        return new ObjectBuilderModel(entityName, (JSONObject) obj);
	}

	private STGroup loadStringTemplateGroup() {
	    return new STGroupFile(getClass().getResource("/builder.stg"), Charsets.UTF_8.toString(), '$', '$');
	}

	public String generate() {
		ST template = templateGroup.getInstanceOf("rootClass")
			.add("package", packageName)
			.add("model", model);
		
		return template.render();
	}
	
	public void writeToFileUnder(String rootDirectory) throws IOException {
		File packageDir = new File(rootDirectory, packageName.replace('.', '/'));
		packageDir.mkdirs();
		File javaFile = new File(packageDir, model.getClassName() + ".java");
		Files.write(generate(), javaFile, Charsets.UTF_8);
	}
}
