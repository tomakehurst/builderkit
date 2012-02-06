package com.github.tomakehurst.builderkit.json;

import static com.github.tomakehurst.builderkit.json.Attribute.ROOT;
import static com.github.tomakehurst.builderkit.json.Attribute.onlyOfType;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.ARRAY;
import static com.github.tomakehurst.builderkit.json.Attribute.Type.OBJECT;
import static com.github.tomakehurst.builderkit.json.BuilderClass.WithMethod.toWithMethod;
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
	
	public List<BuilderClass> getInnerBuilderClasses() {
		return innerBuilderClasses;
	}

	public static BuilderClass fromRootAttribute(final Attribute rootAttribute, final String entityName, final boolean topLevel) {
		final Name name = new Name(entityName);
		
		List<WithMethod> withMethods;
		List<BuilderClass> innerBuilderClasses;
		
		if (rootAttribute instanceof ObjectAttribute) {
			withMethods = newArrayList(transform(
					rootAttribute.asObjectAttribute().getChildAttributes(),
					toWithMethod(name.getBuilderClassName())));
			
			innerBuilderClasses = newArrayList(transform(
					filter(rootAttribute.asObjectAttribute().getChildAttributes(), onlyOfType(OBJECT)),
					toInnerBuilderClass));
			
		} else if (rootAttribute instanceof ArrayAttribute) {
			withMethods = newArrayList(transform(
					newArrayList(rootAttribute),
					toWithMethod(name.getBuilderClassName())));
			
			if (rootAttribute.asArrayAttribute().getElementType() == OBJECT) {
				final BuilderClass innerBuilder = BuilderClass.fromRootAttribute(
				        rootAttribute.asArrayAttribute().getElementAttribute(), "item", false);
				innerBuilderClasses = ImmutableList.of(innerBuilder);
			} else {
				innerBuilderClasses = ImmutableList.of();
			}
			 
		} else {
			throw new IllegalArgumentException("Can only create from an object or array root attribute");
		}
		
		final BuilderClass builderClass = new BuilderClass(name, withMethods, innerBuilderClasses, rootAttribute.getDefaultJson(), topLevel);
		return builderClass;
	}
	
	private static Function<Attribute, BuilderClass> toInnerBuilderClass = new Function<Attribute, BuilderClass>() {
			public BuilderClass apply(final Attribute attribute) {
				return BuilderClass.fromRootAttribute(attribute, attribute.getName().toString(), false);
			}
		};
	
	private BuilderClass(final Name name, final List<WithMethod> withMethods,
	        final List<BuilderClass> innerBuilderClasses, String defaultJson, final boolean topLevel) {
		this.topLevel = topLevel;
		this.name = name;
		this.withMethods = withMethods;
		this.innerBuilderClasses = innerBuilderClasses;
		this.defaultJson = defaultJson;
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
	
	
	
	public static class WithMethod {
		
		private final String returnType;
		private final String name;
		private final String argumentType;
		
		public WithMethod(final String returnType, final String name, final String argumentType) {
			this.returnType = returnType;
			this.name = name;
			this.argumentType = argumentType;
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

		public static Function<Attribute, WithMethod> toWithMethod(final String returnType) {
			return new Function<Attribute, WithMethod>() {
				public WithMethod apply(final Attribute attribute) {
					String argumentType = attribute.getType().getJavaClassNoPackage();
					if (attribute.getType() == OBJECT) {
						argumentType = attribute.getName().getBuilderClassName();
					} else if (attribute.getType() == ARRAY && attribute.asArrayAttribute().getElementType() == OBJECT) {
						argumentType = attribute.getName().getSingularBuilderClassName().replace(ROOT, "Item") + "...";
					} else if (attribute.getType() == ARRAY && attribute.asArrayAttribute().getElementType() != OBJECT) {
						argumentType = attribute.asArrayAttribute().getElementType().getJavaClassNoPackage() + "...";
					}
					
					final String name = attribute.isRoot() ? 
							"withItem" :
							"with" + attribute.getName().getUpperCaseFirstLetterForm();
					
					return new WithMethod(returnType, name, argumentType);
				}
			};
		}

		@Override
		public String toString() {
			return "WithMethod [returnType=" + returnType + ", name=" + name
					+ ", argumentType=" + argumentType + "]";
		}
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
	
	
}