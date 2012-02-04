package com.github.tomakehurst.builderkit.json;

import java.util.List;

import com.github.tomakehurst.builderkit.Name;

public class ObjectAttribute extends Attribute {
	
	private final List<? extends Attribute> childAttributes;
	private final String defaultValueJson;

	public ObjectAttribute(Type type, Name name, String defaultValueJson, List<? extends Attribute> childAttributes) {
		super(type, name);
		this.defaultValueJson = defaultValueJson;
		this.childAttributes = childAttributes;
	}

	public String getDefaultValueJson() {
		return defaultValueJson;
	}

	@SuppressWarnings("unchecked")
	public <T extends Attribute> List<T> getChildAttributes() {
		return (List<T>) childAttributes;
	}

}
