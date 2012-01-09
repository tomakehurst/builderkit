package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Property.Type.OBJECT;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.tomakehurst.builderkit.json.Property.Type;
import com.google.common.base.Predicate;

public class ObjectBuilderModel {

    private final String entityName;
    private final List<Property> properties = newArrayList();
    private final boolean isArray;
    
    @SuppressWarnings("unchecked")
    public ObjectBuilderModel(String entityName, Object obj) {
        this.entityName = entityName;
        
        if (obj instanceof JSONObject) {
            isArray = false;
            JSONObject jsonObject = (JSONObject) obj;
            Set<Map.Entry<String, ?>> attributes = jsonObject.entrySet();
            for (Map.Entry<String, ?> attribute: attributes) {
                properties.add(Property.fromJsonAttribute(attribute));
            }
        } else if (obj instanceof JSONArray) {
            isArray = true;
            JSONArray jsonArray = (JSONArray) obj;
            int i = 0;
            for (Object item: jsonArray) {
                properties.add(Property.fromJsonValue("Anon" + ++i, item));
            }
        } else {
            throw new IllegalArgumentException("Object passed must be either JSONObject or JSONArray");
        }
    }
    
    public Iterable<Property> getProperties() {
        return properties;
    }
    
    public Iterable<Property> getObjectProperties() {
        return newArrayList(filter(properties, onlyObjectProperties()));
    }
    
//    public Iterable<Property> getArrayProperties() {
//        return newArrayList(filter(properties, onlyObjectProperties()));
//    }
    
    public boolean getHasObjectProperties() {
        return size(getObjectProperties()) > 0;
    }
    
    private Predicate<Property> onlyObjectProperties() {
        return new Predicate<Property>() {
            public boolean apply(Property input) {
                return input.getType() == OBJECT || input.getType() == Type.ARRAY;
            }
        };
    }

    public String getEntityName() {
        return entityName;
    }
    
    public String getClassName() {
        return entityName + "Builder";
    }

    public boolean isArray() {
        return isArray;
    }
    
    
}
