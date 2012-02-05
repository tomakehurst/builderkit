package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Attribute.Type.ARRAY;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.OBJECT;

import com.github.tomakehurst.builderkit.Name;

public class ArrayAttribute extends Attribute {
	
	private final Type elementType;
	private final String defaultValueJson;
	private final ObjectAttribute elementAttribute;
	
	public ArrayAttribute(Name name, Type elementType, String defaultValueJson) {
		this(name, elementType, defaultValueJson, null);
	}

	public ArrayAttribute(Name name, Type elementType, String defaultValueJson, ObjectAttribute elementAttribute) {
		super(ARRAY, name);
		this.elementType = elementType;
		this.defaultValueJson = defaultValueJson;
		if (elementType != OBJECT && elementAttribute != null) {
			throw new IllegalArgumentException("Only object arrays can have an element attribute");
		}
		
		this.elementAttribute = elementAttribute;
	}

	public ObjectAttribute getElementAttribute() {
		return elementAttribute;
	}

	public String getDefaultValueJson() {
		return defaultValueJson;
	}

	public Type getElementType() {
		return elementType;
	}

}
