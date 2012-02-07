package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Attribute.ROOT;

import com.google.common.base.Function;

public class WithMethod {
    
    public static enum Type { SINGLE_OBJECT, SINGLE_SCALAR, OBJECT_ARRAY, SCALAR_ARRAY };
	
	private final String returnType;
	private final String name;
	private final String argumentType;
	private final String argumentName;
	private final Type type;
	
	private WithMethod(String returnType, String name, String argumentType, String argumentName, Type type) {
		this.returnType = returnType;
		this.name = name;
		this.argumentType = argumentType;
		this.argumentName = argumentName;
		this.type = type;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getName() {
		return name;
	}

	public String getArgumentType() {
		return argumentType;
	}
	
	public String getSinglularArgumentType() {
	    return argumentType.replace("...", "");
	}
	
	public String getArgumentName() {
        return argumentName;
    }
	
	public boolean isSingleScalar() {
	    return type == Type.SINGLE_SCALAR;
	}
	
	public boolean isScalarArray() {
        return type == Type.SCALAR_ARRAY;
    }
	
	public boolean isSingleObject() {
        return type == Type.SINGLE_OBJECT;
    }
	
	public boolean isObjectArray() {
        return type == Type.OBJECT_ARRAY;
    }

	public static Function<Attribute, WithMethod> toWithMethod(final String returnType) {
		return new Function<Attribute, WithMethod>() {
			public WithMethod apply(Attribute attribute) {
			    if (attribute.isObject()) {
			        return buildForObjectArgument(attribute, returnType);
			    } else if (attribute.isArray()) {
			        return buildForArrayArgument(attribute, returnType);
			    }
			    
			    return buildForScalarArgument(attribute, returnType);
			}
		};
	}
	
	private static WithMethod buildForArrayArgument(Attribute attribute, String returnType) {
        String name = attribute.isRoot() ? 
                "withItems" :
                "with" + attribute.getName().getUpperCaseFirstLetterForm();
        
        if (attribute.isObjectArray()) {
            String argumentName = attribute.isRoot() ? "items" : attribute.getName().getLowerCaseFirstLetterForm();
            String argumentType = attribute.getName().getSingularBuilderClassName().replace(ROOT, "Item") + "...";
            return new WithMethod(returnType, name, argumentType, argumentName, Type.OBJECT_ARRAY);
        } else {
            String argumentName = attribute.isRoot() ? "items" : attribute.getName().getLowerCaseFirstLetterForm();
            String argumentType = attribute.asArrayAttribute().getElementType().getJavaClassNoPackage() + "...";
            return new WithMethod(returnType, name, argumentType, argumentName, Type.SCALAR_ARRAY);
        }
    }
    
    private static WithMethod buildForObjectArgument(Attribute attribute, String returnType) {
        String argumentType = attribute.getName().getBuilderClassName();
        String argumentName = attribute.getName().getLowerCaseFirstLetterForm();
        String name = "with" + attribute.getName().getUpperCaseFirstLetterForm();
        return new WithMethod(returnType, name, argumentType, argumentName, Type.SINGLE_OBJECT);
    }
    
    private static WithMethod buildForScalarArgument(Attribute attribute, String returnType) {
        String name = "with" + attribute.getName().getUpperCaseFirstLetterForm();
        String argumentType = attribute.getType().getJavaClassNoPackage();
        String argumentName = attribute.getName().getLowerCaseFirstLetterForm();
        return new WithMethod(returnType, name, argumentType, argumentName, Type.SINGLE_SCALAR);
    }

	@Override
    public String toString() {
        return "WithMethod [returnType=" + returnType + ", name=" + name + ", argumentType=" + argumentType
                + ", argumentName=" + argumentName + ", type=" + type + "]";
    }
}