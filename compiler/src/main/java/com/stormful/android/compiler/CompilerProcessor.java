package com.stormful.android.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 使用AbstractProcessor生成代码
 */
@AutoService(Processor.class)
public class CompilerProcessor extends AbstractProcessor {

    //文件相关的辅助类
    private Filer mFiler;
    //元素相关的辅助类
    private Elements mElementUtils;
    //日志相关的辅助类
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mInit();
    }

    /**
     * 初始化常用对象
     */
    private void mInit() {
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        printLog("AbstractProcessor:" + "mInit");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        printLog("AbstractProcessor:" + "process");

        //生成一个空的类
        generateEmptyClass();

        //生成一个有方法的类
        generateMehthodClass();

        return true;
    }

    /**
     * 这里必须指定，这个注解处理器是注册给哪个注解的。改方法不执行可能导致process方法不会执行
     * 注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。
     * 换句话说，在这里定义你的注解处理器注册到哪些注解上。
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Override.class.getCanonicalName());
        return types;
    }


    /**
     * 打印log
     *
     * @param msg  信息
     * @param args 参数
     */
    private void printLog(String msg, Object... args) {
        //printLog("Generate file failed, reason: %s", "init");
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    /**
     * 打印log
     *
     * @param msg 信息
     */
    private void printLog(String msg) {
        //printLog("init");
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }


    /**
     * 创建一个空的java类
     */
    private void generateEmptyClass() {
        //创建类
        TypeSpec generateEmptyClass = TypeSpec.classBuilder("GenerateEmptyClass")
                .addModifiers(Modifier.PUBLIC)
                .build();
        //创建类存放的包名
        JavaFile javaFile = JavaFile.builder("com.stormful.android.abstractprocessordemo", generateEmptyClass).build();

        //创建类
        try {
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateMehthodClass() {

        String className = "GenerateMethodClass";
        String text = "我是返回值,在getTextMethod方法中";

        StringBuilder builder = new StringBuilder()
                .append("package com.autotestdemo.maomao.autotestdemo.auto;\n\n")
                .append("public class ")
                .append(className)
                .append(" {\n\n") // open class
                .append("\tpublic String getTextMethod() {\n") // open method
                .append("\t\treturn \"");

        // this is appending to the return statement
        builder.append(text).append(" !");


        builder.append("\";\n") // end returne
                .append("\t}\n") // close method
                .append("}\n"); // close class


        try { // write the file
            JavaFileObject source = mFiler.createSourceFile("com.stormful.android.abstractprocessordemo." + className);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
            e.printStackTrace();
        }

    }

}
