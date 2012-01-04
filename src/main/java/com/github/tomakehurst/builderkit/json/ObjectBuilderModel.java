package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Property.Type.OBJECT;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import com.google.common.base.Predicate;

public class ObjectBuilderModel {

    private final String entityName;
    private final List<Property> properties = newArrayList();
    
    @SuppressWarnings("unchecked")
    public ObjectBuilderModel(String entityName, JSONObject obj) {
        this.entityName = entityName;
        
        JSONObject jsonObject = obj;
        Set<Map.Entry<String, ?>> attributes = jsonObject.entrySet();
        for (Map.Entry<String, ?> attribute: attributes) {
            properties.add(Property.fromJsonAttribute(attribute));
        }
    }
    
    public Iterable<Property> getProperties() {
        return properties;
    }
    
    public Iterable<Property> getObjectProperties() {
        return newArrayList(filter(properties, onlyObjectProperties()));
    }
    
    public boolean getHasObjectProperties() {
        return size(getObjectProperties()) > 0;
    }
    
    private Predicate<Property> onlyObjectProperties() {
        return new Predicate<Property>() {
            public boolean apply(Property input) {
                return input.getType() == OBJECT;
            }
        };
    }

    public String getEntityName() {
        return entityName;
    }
    
    public String getClassName() {
        return entityName + "Builder";
    }
    
    
}
