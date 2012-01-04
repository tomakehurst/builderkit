package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Utils.firstCharToUppercase;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Property {

	private Type type;
	private String javaClassName;
    private String name;
	private Object defaultValue;
	
	public static Property fromJsonAttribute(Map.Entry<String, ?> attribute) {
		return new Property(Type.fromClass(attribute.getValue().getClass()), attribute.getKey(), attribute.getValue());
	}
	
	public Property(String javaClass, String name, Object defaultValue) {
        this.javaClassName = javaClass;
        this.name = name;
        this.defaultValue = defaultValue;
    }
	
	public Property(Type type, String name, Object defaultValue) {
		this.type = type;
		if (type == Type.OBJECT) {
		    javaClassName = firstCharToUppercase(name) + "Builder";
		    this.defaultValue = new ObjectBuilderModel(firstCharToUppercase(name), (JSONObject) defaultValue);
		} else if (type == Type.ARRAY) {
		    //TODO: finish
		    javaClassName = "List";
		} else {
		    javaClassName = type.getJavaClassNoPackage();
		    this.defaultValue = defaultValue;
		}
		this.name = name;
	}

	public Type getType() {
		return type;
	}
	
    public String getJavaClassName() {
        return javaClassName;
    }
    
	public String getName() {
		return name;
	}
	
	public String getNameFirstLetterUppercase() {
		return firstCharToUppercase(name);
	}
	
	public boolean isObject() {
	    return type == Type.OBJECT;
	}
	
	public boolean isArray() {
	    return type == Type.ARRAY;
	}

	@SuppressWarnings("unchecked")
	public <T> T getDefaultValue() {
		return (T) defaultValue;
	}
	
	public String getDefaultValueEscaped() {
	    if (type == Type.STRING) {
	        return '"' + String.valueOf(defaultValue) + '"';
	    } else {
	        return String.valueOf(defaultValue);
	    }
	}
	
	public ObjectBuilderModel getModel() {
	    return (ObjectBuilderModel) defaultValue;
	}
	
	public static enum Type { 
		STRING(String.class),
		LONG(Long.class),
		DOUBLE(Double.class),
		BOOLEAN(Boolean.class),
		OBJECT(JSONObject.class),
		ARRAY(JSONArray.class);
	
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
        return "Property [getType()=" + getType() + ", getJavaClassName()=" + getJavaClassName() + ", getName()="
                + getName() + ", getNameFirstLetterUppercase()=" + getNameFirstLetterUppercase() + ", isObject()="
                + isObject() + ", isArray()=" + isArray() + ", getDefaultValue()=" + getDefaultValue() + "]";
    }

}
