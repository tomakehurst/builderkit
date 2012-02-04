package com.github.tomakehurst.builderkit.json;

import com.github.tomakehurst.builderkit.Name;

public class BuilderClass {
	
	private boolean topLevel;
	private Name name;
	
	public static class WithMethod {
		
		private Name name;
		private Attribute.Type argumentType;
		private BuilderClass argumentBuilder;
		private boolean array;
		
		
	}
	
	public boolean requiresJsonObjectImport() {
		return false;
	}
	
	public boolean requiresJsonArrayImport() {
		return false;
	}
	
	public boolean isTopLevel() {
		return topLevel;
	}
}