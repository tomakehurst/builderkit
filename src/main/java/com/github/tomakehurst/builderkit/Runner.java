package com.github.tomakehurst.builderkit;

import static com.google.common.base.Charsets.UTF_8;

import java.io.File;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import com.github.tomakehurst.builderkit.json.JsonBuilderGenerator;
import com.google.common.io.Files;

public class Runner {

    public static void main(String... args) throws Exception {
        OptionParser optionParser = new OptionParser();
        optionParser.accepts("package", "The Java package for the builder").withRequiredArg();
        optionParser.accepts("name", "The name of the entity e.g. EggSandwich will produce EggSandwichBuilder").withRequiredArg();
        optionParser.accepts("json-file", "The JSON file from which to generate the builder").withRequiredArg();
        optionParser.accepts("sourceDir", "The source directory. Defaults to the current directory if not specified").withRequiredArg();
        OptionSet optionSet = optionParser.parse(args);
        
        if (!optionSet.has("package") || !optionSet.has("package") || !optionSet.has("package")) {
            optionParser.printHelpOn(System.out);
            return;
        }
        
        File jsonFile = new File((String) optionSet.valueOf("json-file"));
        String name = (String) optionSet.valueOf("name");
        String packageName = (String) optionSet.valueOf("package");
        String sourceDirectory = new File(".").getAbsolutePath();
        if (optionSet.has("sourceDir")) {
            sourceDirectory = (String) optionSet.valueOf("sourceDir");
        }
        
        JsonBuilderGenerator generator = new JsonBuilderGenerator(packageName, name, Files.toString(jsonFile, UTF_8));
        generator.writeToFileUnder(sourceDirectory);
        
        System.out.println("Done");
    }
}
