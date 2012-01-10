package com.github.tomakehurst.builderkit.json;

public class Name {

    private String source;

    public Name(String source) {
        this.source = Character.toLowerCase(source.charAt(0)) + source.substring(1);
    }
    
    public String getUpperCaseFirstLetterForm() {
        return Character.toUpperCase(source.charAt(0)) + source.substring(1);
    }
    
    public String getLowerCaseFirstLetterForm() {
        return source;
    }
    
    public String getNonPluralForm() {
        return toSingluar(source);
    }
    
    public String getIndefiniteArticleForm() {
        if ("aeiou".contains("" + source.charAt(0))) {
            return "an" + toSingluar(getUpperCaseFirstLetterForm());
        }
        
        return "a" + toSingluar(getUpperCaseFirstLetterForm());
    }
    
    public String getBuilderClassName() {
        return getUpperCaseFirstLetterForm() + "Builder";
    }
    
    public String getSingularBuilderClassName() {
        return toSingluar(getUpperCaseFirstLetterForm()) + "Builder";
    }
    
    private static String toSingluar(String str) {
        return Inflector.getInstance().singularize(str);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        Name other = (Name) obj;
        if (source == null) {
            if (other.source != null) {
                return false;
            }
        } else if (!source.equals(other.source)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return source;
    }
}
