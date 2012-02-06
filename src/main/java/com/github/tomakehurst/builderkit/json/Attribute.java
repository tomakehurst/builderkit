package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Attribute.Type.OBJECT;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.tomakehurst.builderkit.Name;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class Attribute {
	
	protected static final String ROOT = "$$$root$$$";

	private final Type type;
    private final Name name;

    private final String defaultJson;
    
    @SuppressWarnings("unchecked")
	public static <T extends Attribute> T fromJsonAttribute(Map.Entry<String, ?> attribute) {
    	Name name = new Name(attribute.getKey());
    	Type type = Type.fromClass(attribute.getValue().getClass());
    	Object value = attribute.getValue();
    	if (value instanceof JSONArray) {
    		Type elementType = typeOfFirstElementIn((JSONArray) value);
    		if (elementType == OBJECT) {
    			JSONObject firstObject = firstObjectIn((JSONArray) value);
    			ObjectAttribute elementAttribute = new ObjectAttribute(
    					new Name(name.getNonPluralForm()), firstObject.toString(), childAttributesOf(firstObject));
    			return (T) new ArrayAttribute(name, elementType, value.toString(), elementAttribute);
    		}
    		
    		return (T) new ArrayAttribute(name, elementType, value.toString());
    		
    	} else if (value instanceof JSONObject) {
    		return (T) new ObjectAttribute(name, value.toString(), childAttributesOf((JSONObject) value));
    	}
    	
    	return (T) new Attribute(type, name, attribute.getValue().toString());
    }
    
    private static Type typeOfFirstElementIn(JSONArray array) {
    	if (array.size() > 0) {
    		return Type.fromClass(array.get(0).getClass());
    	}
    	
    	return Type.STRING;
    }
    
    private static JSONObject firstObjectIn(JSONArray array) {
    	return (JSONObject) array.get(0);
    }
    
    @SuppressWarnings("unchecked")
	private static List<Attribute> childAttributesOf(JSONObject obj) {
    	return newArrayList(transform(obj.entrySet(), new Function<Map.Entry<String, ?>, Attribute>() {
			public Attribute apply(Entry<String, ?> input) {
				return Attribute.fromJsonAttribute(input);
			}
    	}));
    }
    
    @Override
	public String toString() {
		return "Attribute [type=" + type + ", name=" + name + "]";
	}

	public Attribute(Type type, Name name, String defaultJson) {
		this.type = type;
		this.name = name;
		this.defaultJson = defaultJson;
	}

	public Type getType() {
		return type;
	}

	public Name getName() {
		return name;
	}
	
	public boolean isRoot() {
		return getName().toString().equals(ROOT);
	}
	
	public ArrayAttribute asArrayAttribute() {
		return (ArrayAttribute) this;
	}
	
	public ObjectAttribute asObjectAttribute() {
		return (ObjectAttribute) this;
	}
	
	public static Predicate<Attribute> onlyOfType(final Type type) {
		return new Predicate<Attribute>() {
			public boolean apply(Attribute attribute) {
				return attribute.getType() == type;
			}
		};
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

    public String getDefaultJson() {
        return defaultJson;
    }
}
