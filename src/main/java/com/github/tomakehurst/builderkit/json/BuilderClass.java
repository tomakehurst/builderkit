package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Attribute.onlyObjectArrays;
import static com.github.tomakehurst.builderkit.json.Attribute.onlyOfType;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.OBJECT;
import static com.github.tomakehurst.builderkit.json.WithMethod.toWithMethod;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.github.tomakehurst.builderkit.Name;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

public class BuilderClass {
	
	private final boolean topLevel;
	private final Name name;
	private final List<WithMethod> withMethods;
	private final List<BuilderClass> innerBuilderClasses;
	private final String defaultJson;
	private final boolean array;
	
	public List<BuilderClass> getInnerBuilderClasses() {
		return innerBuilderClasses;
	}

	public static BuilderClass fromRootAttribute(Attribute rootAttribute, String entityName, boolean topLevel) {
		Name name = new Name(entityName);
		
		if (rootAttribute instanceof ObjectAttribute) {
		    return createForObject(rootAttribute, topLevel, name);
		} else if (rootAttribute instanceof ArrayAttribute) {
		    return createForArray(rootAttribute, topLevel, name);
		} else {
			throw new IllegalArgumentException("Can only create from an object or array root attribute");
		}
	}

    private static BuilderClass createForArray(Attribute rootAttribute, boolean topLevel, Name name) {
        List<WithMethod> withMethods = newArrayList(
            transform(
        		newArrayList(rootAttribute),
        		toWithMethod(name.getBuilderClassName())));
        
        List<BuilderClass> innerBuilderClasses;
        if (rootAttribute.asArrayAttribute().getElementType() == OBJECT) {
        	BuilderClass innerBuilder = BuilderClass.fromRootAttribute(
        	        rootAttribute.asArrayAttribute().getElementAttribute(), "item", false);
        	innerBuilderClasses = ImmutableList.of(innerBuilder);
        } else {
        	innerBuilderClasses = ImmutableList.of();
        }
        
        return new BuilderClass(name, withMethods, innerBuilderClasses, rootAttribute.getDefaultJson(),
                topLevel, rootAttribute.isArray());
    }

    private static BuilderClass createForObject(Attribute rootAttribute, boolean topLevel, Name name) {
        List<Attribute> childAttributes = rootAttribute.asObjectAttribute().getChildAttributes();
        
        List<WithMethod> withMethods = newArrayList(
            transform(
        		childAttributes,
        		toWithMethod(name.getBuilderClassName())));
        
        List<BuilderClass> innerBuilderClasses = newArrayList(
                concat(
        	        transform(
        				filter(childAttributes, onlyOfType(OBJECT)),
        				toInnerBuilderClass),
        			transform(
                        filter(childAttributes, onlyObjectArrays()),
                        toInnerBuilderClassFromArray)));
        
        return new BuilderClass(name, withMethods, innerBuilderClasses, rootAttribute.getDefaultJson(),
                topLevel, rootAttribute.isArray());
    }
	
	private static Function<Attribute, BuilderClass> toInnerBuilderClass = new Function<Attribute, BuilderClass>() {
		public BuilderClass apply(Attribute attribute) {
			return BuilderClass.fromRootAttribute(attribute, attribute.getName().toString(), false);
		}
	};
		
	private static Function<Attribute, BuilderClass> toInnerBuilderClassFromArray = new Function<Attribute, BuilderClass>() {
        public BuilderClass apply(Attribute attribute) {
            Attribute arrayElementAttribute = attribute.asArrayAttribute().getElementAttribute();
            return BuilderClass.fromRootAttribute(arrayElementAttribute, attribute.getName().getNonPluralForm(), false);
        }
    };
	
	private BuilderClass(Name name, List<WithMethod> withMethods,
	        List<BuilderClass> innerBuilderClasses, String defaultJson, boolean topLevel, boolean array) {
		this.topLevel = topLevel;
		this.name = name;
		this.withMethods = withMethods;
		this.innerBuilderClasses = innerBuilderClasses;
		this.defaultJson = defaultJson;
		this.array = array;
	}

	public List<WithMethod> getWithMethods() {
		return withMethods;
	}

	public Name getName() {
		return name;
	}
	
	public String getStaticFactoryMethodName() {
	    return name.getIndefiniteArticleForm();
	}
	
	public String getStaticFactoryReturnType() {
	    return name.getBuilderClassName();
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

    public String getDefaultJson() {
        return defaultJson;
    }
    
    public String getJsonSourceStringEscaped() {
        return defaultJson.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public boolean isArray() {
        return array;
    }
	
	
}