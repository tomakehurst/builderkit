package com.github.tomakehurst.builderkit.json;

import java.io.File;
import java.io.IOException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.github.tomakehurst.builderkit.Name;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class JsonBuilderGenerator2 {

	private final String packageName;
	private final STGroup templateGroup;
	private final Name name;
	private final JsonDocumentModel documentModel;

	public JsonBuilderGenerator2(final String packageName, final String entityName, final String sourceJson) throws ParseException {
		this.packageName = packageName;
		this.name = new Name(entityName);
		templateGroup = loadStringTemplateGroup();
		documentModel = JsonDocumentModel.createFrom(new JSONParser().parse(sourceJson));
	}
	
	private STGroup loadStringTemplateGroup() {
	    return new STGroupFile(getClass().getResource("/builder2.stg"), Charsets.UTF_8.toString(), '$', '$');
	}

	public String generate() {
	    final BuilderClass builderClassModel = BuilderClass.fromRootAttribute(documentModel.getRootAttribute(), name.toString(), true);
	    final String templateName = documentModel.isArrayRooted() ? "listRootClass" : "objectRootClass";
		final ST template = templateGroup.getInstanceOf(templateName)
			.add("package", packageName)
			.add("documentModel", builderClassModel);
		
		return template.render();
	}
	
	public void writeToFileUnder(final String rootDirectory) throws IOException {
		final File packageDir = new File(rootDirectory, packageName.replace('.', '/'));
		packageDir.mkdirs();
		final File javaFile = new File(packageDir, name.getBuilderClassName() + ".java");
		Files.write(generate(), javaFile, Charsets.UTF_8);
	}
}
