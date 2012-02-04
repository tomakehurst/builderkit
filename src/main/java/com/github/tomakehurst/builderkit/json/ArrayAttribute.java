package com.github.tomakehurst.builderkit.json;

import java.util.List;

import com.github.tomakehurst.builderkit.Name;

public class ArrayAttribute extends Attribute {
	
	private final Type elementType;
	private final String defaultValueJson;
	private final List<Attribute> elementAttributes;

	public ArrayAttribute(Type type, Name name, Type elementType, String defaultValueJson, List<Attribute> elementAttributes) {
		super(type, name);
		this.elementType = elementType;
		this.defaultValueJson = defaultValueJson;
		this.elementAttributes = elementAttributes;
	}

	public List<Attribute> getElementAttributes() {
		return elementAttributes;
	}

	public String getDefaultValueJson() {
		return defaultValueJson;
	}

	public Type getElementType() {
		return elementType;
	}

}
