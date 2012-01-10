package com.github.tomakehurst.builderkit.json;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.tomakehurst.builderkit.Name;

public class NameTest {

    @Test
    public void generatesUppercaseFirstLetterName() {
        Name name = new Name("flipFlapFlop");
        assertThat(name.getUpperCaseFirstLetterForm(), is("FlipFlapFlop"));
    }
    
    @Test
    public void generatesLowerCaseFirstLetterName() {
        Name name = new Name("flipFlapFlop");
        assertThat(name.getLowerCaseFirstLetterForm(), is("flipFlapFlop"));
    }
    
    @Test
    public void generatesNonPluralFormWhenPlural() {
        Name name = new Name("flipFlapFlops");
        assertThat(name.getNonPluralForm(), is("flipFlapFlop"));
    }
    
    @Test
    public void generatesNonPluralFormWhenNotPlural() {
        Name name = new Name("FlipFlapFlop");
        assertThat(name.getNonPluralForm(), is("flipFlapFlop"));
    }
    
    @Test
    public void generatesIndefiniteArticleFormWhenFirstLetterIsConsonant() {
        Name name = new Name("flipFlapFlop");
        assertThat(name.getIndefiniteArticleForm(), is("aFlipFlapFlop"));
    }
    
    @Test
    public void generatesIndefiniteArticleFormWhenFirstLetterIsVowel() {
        Name name = new Name("eggSandwich");
        assertThat(name.getIndefiniteArticleForm(), is("anEggSandwich"));
    }
    
    @Test
    public void generatesBuilderClassName() {
        Name name = new Name("eggSandwich");
        assertThat(name.getBuilderClassName(), is("EggSandwichBuilder"));
    }
    
    
}
