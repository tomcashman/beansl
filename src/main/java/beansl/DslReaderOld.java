/**
 * Copyright 2015 Thomas Cashman
 */
package beansl;

import groovy.lang.Binding;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import beansl.util.StringUtils;

/**
 *
 * @author Thomas Cashman
 */
public class DslReaderOld<T> {
    private static final String BEAN_SL_VAR = "beanSL";

    private final Class<T> clazz;
    private final String root;

    private final Binding binding;
    private GroovyShell groovyShell;

    public DslReaderOld(Class<T> clazz) {
        this.clazz = clazz;

        root = StringUtils.toCamelCase(clazz.getSimpleName());
        binding = new Binding();
        try {
            binding.setVariable(root, clazz.newInstance());
            groovyShell = new GroovyShell(clazz.getClassLoader(), binding);
            groovyShell.evaluate("ExpandoMetaClass.enableGlobally();");
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

    public T read(Path path) throws IOException {
        List<String> contents = Files.readAllLines(path, Charset.defaultCharset());
        StringBuilder contentResult = new StringBuilder();
        for (String line : contents) {
            contentResult.append(line + "\n");
        }
        return read(contentResult.toString());
    }

    public T read(Reader dslReader) throws IOException {
        Script script = groovyShell.parse(dslReader);
        script.run();
        return (T) groovyShell.getProperty(root);
    }

    public T read(File dslFile) throws IOException {
        Script script = groovyShell.parse(dslFile);
        script.run(new File(getClass().getClassLoader().getResource("BeanSL.grvy").getFile()), null);
        script.invokeMethod("convertClassToDsl", clazz);
        script.run();
        return (T) groovyShell.getProperty(root);
    }

    public T read(String dsl) {
        Script script = groovyShell.parse(dsl);
        script.invokeMethod("convertClassToDsl", clazz);
        script.run();
        return (T) groovyShell.getProperty(root);
    }
}
