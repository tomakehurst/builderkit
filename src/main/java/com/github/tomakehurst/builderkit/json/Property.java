package com.github.tomakehurst.builderkit.json;

import java.util.Map;

public class Property {

	private Type type;
	private String name;
	private Object defaultValue;
	
	public static Property fromJsonAttribute(Map.Entry<String, ?> attribute) {
		return new Property(Type.fromClass(attribute.getValue().getClass()), attribute.getKey(), attribute.getValue());
	}
	
	public Property(Type type, String name, Object defaultValue) {
		this.type = type;
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public <T> T getDefaultValue() {
		return (T) defaultValue;
	}
	
	public enum Type { 
		STRING(String.class),
		LONG(Long.class),
		DOUBLE(Double.class),
		BOOLEAN(Boolean.class);
	
		private final Class<?> javaClass;

		private Type(Class<?> javaClass) {
			this.javaClass = javaClass;
		}

		public Class<?> getJavaClass() {
			return javaClass;
		}
		
		public String getJavaClassNoPackage() {
			String fullName = javaClass.getName();
			return fullName.substring(fullName.lastIndexOf('.') + 1);
		}
		
		public static Type fromClass(Class<?> javaClass) {
			for (Type type: values()) {
				if (type.getJavaClass().isAssignableFrom(javaClass)) {
					return type;
				}
			}
			
			throw new IllegalArgumentException(javaClass.getName() + " isn't supported");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Property other = (Property) obj;
		if (defaultValue == null) {
			if (other.defaultValue != null) {
				return false;
			}
		} else if (!defaultValueEquals(other.defaultValue)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
	private boolean defaultValueEquals(Object otherDefaultValue) {
		if (defaultValue instanceof Number) {
			return ((Number) defaultValue).doubleValue() == ((Number) otherDefaultValue).doubleValue();
		} else {
			return defaultValue.equals(otherDefaultValue);
		}
	}
	
	@Override
	public String toString() {
		return "Property [type=" + type + ", name=" + name + ", defaultValue="
				+ defaultValue + "]";
	};
	
}
