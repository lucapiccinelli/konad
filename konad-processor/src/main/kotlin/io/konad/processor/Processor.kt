package io.konad.processor

import com.google.auto.service.AutoService
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(javax.annotation.processing.Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes(value = ["io.konad.annotations.Curry"])
class Processor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: return false
        logInfo("processing KAPT, output to $kaptKotlinGeneratedDir")

        val annotation = io.konad.annotations.Curry::class.java
        processAnnotation(roundEnv, kaptKotlinGeneratedDir, annotation){
            val curry = it.getAnnotation(annotation)
            val alphabet = GenericsHelper.generateAlphabet(SimpleCurry.baseAlphabet, curry.argsNum)

            (2 until curry.argsNum).joinToString(System.lineSeparator()) { i ->
                JvmCurry(alphabet).generate(i)
            }
        }

        return true
    }

    private fun <T: Annotation> processAnnotation(
        roundEnv: RoundEnvironment,
        outputDir: String,
        annotation: Class<T>,
        body: (Element) -> String
    ) {
        roundEnv.getElementsAnnotatedWith(annotation)
            .map(body)
            .forEach { fileContent ->
                val outPath = Path.of(outputDir, "io", "konad")
                if(!Files.exists(outPath)){
                    Files.createDirectories(outPath)
                }
                val file = Path.of(outPath.toString(), "${annotation.simpleName.lowercase()}_generated.kt").toFile()
                file.writeText("package io.konad${System.lineSeparator()}$fileContent", Charsets.UTF_8)
            }
    }

    private fun logInfo(message: String) {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, message)
    }
}