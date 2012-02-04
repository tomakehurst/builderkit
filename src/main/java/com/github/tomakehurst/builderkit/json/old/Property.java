package com.github.tomakehurst.builderkit.json.old;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.tomakehurst.builderkit.Name;

public class Property {

	private Type type;
	private String javaClassName;
    private Name name;
	private Object defaultValue;
	
	public static Property fromJsonAttribute(Map.Entry<String, ?> attribute) {
		return new Property(Type.fromClass(attribute.getValue().getClass()), attribute.getKey(), attribute.getValue());
	}
	
	public static Property fromJsonValue(String key, Object value) {
        return new Property(Type.fromClass(value.getClass()), key, value);
    }
	
	public Property(String javaClass, String name, Object defaultValue) {
        this.javaClassName = javaClass;
        this.name = new Name(name);
        this.defaultValue = defaultValue;
    }
	
	public Property(Type type, String propertyName, Object defaultValue) {
		this.type = type;
		this.name = new Name(propertyName);
		if (type == Type.OBJECT) {
		    javaClassName = name.getBuilderClassName();
		    this.defaultValue = new ObjectBuilderModel(name, (JSONObject) defaultValue);
		} else if (type == Type.OBJECT_ARRAY) {
		    javaClassName = name.getBuilderClassName();
		    Object firstValue = ((JSONArray) defaultValue).get(0);
		    if (firstValue instanceof JSONObject) {
		    	JSONObject firstValueObject = (JSONObject) firstValue;
			    this.defaultValue = new ObjectBuilderModel(name, firstValueObject);
		    } else {
		    	this.defaultValue = firstValue;
		    }
		} else {
		    javaClassName = type.getJavaClassNoPackage();
		    this.defaultValue = defaultValue;
		}
	}

	public Type getType() {
		return type;
	}
	
    public String getJavaClassName() {
        return javaClassName;
    }
    
	public Name getName() {
		return name;
	}
	
	public boolean isObject() {
	    return type == Type.OBJECT;
	}
	
	public boolean isArray() {
	    return type == Type.OBJECT_ARRAY;
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
		OBJECT_ARRAY(JSONArray.class),
		SCALAR_ARRAY(JSONArray.class);
	
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

	private boolean defaultValueEquals(Object otherDefaultValue) {
		if (defaultValue instanceof Number) {
			return ((Number) defaultValue).doubleValue() == ((Number) otherDefaultValue).doubleValue();
		} else {
			return defaultValue.equals(otherDefaultValue);
		}
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result + ((javaClassName == null) ? 0 : javaClassName.hashCode());
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
        if (javaClassName == null) {
            if (other.javaClassName != null) {
                return false;
            }
        } else if (!javaClassName.equals(other.javaClassName)) {
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

    @Override
    public String toString() {
        return "Property [getType()=" + getType() + ", getJavaClassName()=" + getJavaClassName() + ", getName()="
                + getName() + ", isObject()=" + isObject() + ", isArray()=" + isArray() + ", getDefaultValue()="
                + getDefaultValue() + ", getDefaultValueEscaped()=" + getDefaultValueEscaped() + ", getModel()="
                + getModel() + "]";
    }
}
