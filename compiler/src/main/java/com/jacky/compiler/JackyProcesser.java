package com.jacky.compiler;

import com.google.auto.service.AutoService;
import com.jacky.annotations.ApplicationContext;

import java.io.IOException;
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
        mMessage.printMessage(Diagnostic.Kind.ERROR, "start message");
        StringBuilder builder = new StringBuilder().append("package com.jacky.util;\n\n")
                .append("public class testClass {\n\n")
                // open class
                .append("\tpublic String getMessage() {\n") // open method
                .append("\t\treturn \""); // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element: roundEnvironment.getElementsAnnotatedWith(ApplicationContext.class)) {
            String objectType = element.getSimpleName().toString();
//          this is appending to the return statement
            builder.append(objectType).append(" says hello!\\n");
        }
        builder.append("\";\n")
                // end return
                .append("\t}\n") // close method
                .append("}\n"); // close class
        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.jacky.util.testClass");
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) { // Note: calling e.printStackTrace() will print IO errors // that occur from the file already existing after its first run, this is normal
        }
        return true;
    }
}
