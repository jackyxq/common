package com.jacky.processer;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;

@SupportedAnnotationTypes({"com.jacky.processer.ApplicationContext"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class JackyProcesser extends AbstractProcessor {

    private Messager mMessage;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessage = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("process");
        mMessage.printMessage(Diagnostic.Kind.ERROR, "start message");
        for (Element element : roundEnvironment.getElementsAnnotatedWith(ApplicationContext.class)) {
            if (element.getKind() == ElementKind.FIELD) {
                mMessage.printMessage(Diagnostic.Kind.NOTE, "printMessage : " + element.toString());
            }
        }
        return true;
    }
}
