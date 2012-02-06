package com.github.tomakehurst.builderkit.json;

import java.util.List;

import com.github.tomakehurst.builderkit.Name;

public class ObjectAttribute extends Attribute {
	
	private final List<? extends Attribute> childAttributes;
	public ObjectAttribute(Name name, String defaultJson, List<? extends Attribute> childAttributes) {
		super(Type.OBJECT, name, defaultJson);
		this.childAttributes = childAttributes;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Attribute> List<T> getChildAttributes() {
		return (List<T>) childAttributes;
	}

}
