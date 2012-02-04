package com.github.tomakehurst.builderkit.json;

import java.util.AbstractMap;


public class JsonDocumentModel {
	
	private boolean arrayRooted;
	private Attribute rootAttribute;

	public static JsonDocumentModel createFrom(Object rootJsonObject) {
		JsonDocumentModel model = new JsonDocumentModel();
		model.rootAttribute = Attribute.fromJsonAttribute(new RootJsonAttribute(rootJsonObject));
		model.arrayRooted = (model.rootAttribute instanceof ArrayAttribute);
		return model;
	}
	
	@SuppressWarnings("serial")
	private static class RootJsonAttribute extends AbstractMap.SimpleImmutableEntry<String, Object> {
		public RootJsonAttribute(Object value) {
			super("$$$root$$$", value);
		}
	}
	
	public boolean isArrayRooted() {
		return arrayRooted;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Attribute> T getRootAttribute() {
		return (T) rootAttribute;
	}

}
