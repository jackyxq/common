package com.jacky.compiler;

import com.google.auto.service.AutoService;
import com.jacky.annotations.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public final class JackyProcesser extends AbstractProcessor {

    private Messager mMessage;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(ApplicationContext.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessage = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("process");
        mMessage.printMessage(Diagnostic.Kind.NOTE, "start message");

        for (Element element: roundEnvironment.getElementsAnnotatedWith(ApplicationContext.class)) {
            if(element.getKind() != ElementKind.METHOD) {
                mMessage.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Only method can be annotated with @%s", ApplicationContext.class.getName()));
                return true;
            }
            String packageName = element.asType().toString().replace("()", "");
            String methodName = element.getSimpleName().toString();

            String values[] = element.getAnnotation(ApplicationContext.class).value();
            for(String s : values) {
                if(ApplicationContext.SharePrefence.equals(s)) {
                    String name = "com.jacky.util.PreferenceUtils";
                    String content = readTemplete(name);
                    generateJavaFile(name, String.format(content, packageName + "." + methodName + "()"));
                }
            }
        }
        return true;
    }

    private void generateJavaFile(String name, String content) {
        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile(name);
            Writer writer = source.openWriter();
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }

    private String readTemplete(String name) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        FileInputStream stream = null;
        byte[] bytes = new byte[1024];
        int i = 0;
        File file = new File("compiler/src/main/tpl/" + name);
        String path = file.getAbsolutePath();
        mMessage.printMessage(Diagnostic.Kind.WARNING, path);
        try {
            stream = new FileInputStream(file);
            while (true) {
                i = stream.read(bytes);
                if(i == -1) break;
                writer.write(bytes, 0, i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return writer.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return writer.toString();
        }
    }
}
