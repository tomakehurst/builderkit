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
	
	public List<BuilderClass> getInnerBuilderClasses() {
		return innerBuilderClasses;
	}

	public static BuilderClass fromRootAttribute(Attribute rootAttribute, String entityName) {
		Name name = new Name(entityName);
		
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
				BuilderClass innerBuilder = BuilderClass.fromRootAttribute(rootAttribute.asArrayAttribute().getElementAttribute(), "item");
				innerBuilderClasses = ImmutableList.of(innerBuilder);
			} else {
				innerBuilderClasses = ImmutableList.of();
			}
			 
		} else {
			throw new IllegalArgumentException("Can only create from an object or array root attribute");
		}
		
		BuilderClass builderClass = new BuilderClass(name, withMethods, innerBuilderClasses, true);
		return builderClass;
	}
	
	private static Function<Attribute, BuilderClass> toInnerBuilderClass = new Function<Attribute, BuilderClass>() {
			public BuilderClass apply(Attribute attribute) {
				return BuilderClass.fromRootAttribute(attribute, attribute.getName().toString());
			}
		};
	
	private BuilderClass(Name name, List<WithMethod> withMethods, List<BuilderClass> innerBuilderClasses, boolean topLevel) {
		this.topLevel = topLevel;
		this.name = name;
		this.withMethods = withMethods;
		this.innerBuilderClasses = innerBuilderClasses;
	}

	public List<WithMethod> getWithMethods() {
		return withMethods;
	}

	public Name getName() {
		return name;
	}

	public static class WithMethod {
		
		private String returnType;
		private String name;
		private String argumentType;
		
		public WithMethod(String returnType, String name, String argumentType) {
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
				public WithMethod apply(Attribute attribute) {
					String argumentType = attribute.getType().getJavaClassNoPackage();
					if (attribute.getType() == OBJECT) {
						argumentType = attribute.getName().getBuilderClassName();
					} else if (attribute.getType() == ARRAY && attribute.asArrayAttribute().getElementType() == OBJECT) {
						argumentType = attribute.getName().getSingularBuilderClassName().replace(ROOT, "Item") + "...";
					} else if (attribute.getType() == ARRAY && attribute.asArrayAttribute().getElementType() != OBJECT) {
						argumentType = attribute.asArrayAttribute().getElementType().getJavaClassNoPackage() + "...";
					}
					
					String name = attribute.isRoot() ? 
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
	
	
}