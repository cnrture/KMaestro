package com.github.cnrture.kmaestro.actions

import com.github.cnrture.kmaestro.services.MaestroService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import java.io.File
import java.lang.reflect.Method
import javax.swing.SwingUtilities

object KMaestroActionHandler {

    fun createYamlFile(functionElement: PsiElement, project: Project) {
        val functionName = extractFunctionName(functionElement)
        Messages.showInfoMessage(
            project,
            "Creating YAML file for function: $functionName",
            "Create YAML File"
        )
        // TODO: YAML file creation logic will be implemented here
    }

    fun runMaestroTest(functionElement: PsiElement, project: Project) {
        val functionName = extractFunctionName(functionElement)

        try {
            // Fonksiyonu çalıştır
            executeFunctionByName(functionElement, functionName, project)
        } catch (e: Exception) {
            Messages.showErrorDialog(
                project,
                "Error running function '$functionName': ${e.message}",
                "Run Maestro Test Error"
            )
        }
    }

    private fun executeFunctionByName(functionElement: PsiElement, functionName: String, project: Project) {
        ApplicationManager.getApplication().runReadAction {
            try {
                // Fonksiyon çalıştırma mantığı
                invokeFunctionDynamically(functionElement, functionName, project)
            } catch (e: Exception) {
                Messages.showErrorDialog(
                    project,
                    "Failed to execute function: ${e.message}",
                    "Execution Error"
                )
            }
        }
    }

    private fun invokeFunctionDynamically(functionElement: PsiElement, functionName: String, project: Project) {
        try {
            // Fonksiyonun bulunduğu dosyayı al
            val containingFile = functionElement.containingFile
            val packageName = getPackageName(containingFile)
            val className = getClassName(containingFile)

            // Dinamik olarak fonksiyonu çağır
            val fullClassName = if (packageName.isNotEmpty()) "$packageName.$className" else className

            // Gerçek fonksiyon çalıştırma işlemi
            executeKotlinFunction(fullClassName, functionName, project)

        } catch (e: Exception) {
            throw RuntimeException("Failed to invoke function dynamically: ${e.message}", e)
        }
    }

    private fun executeKotlinFunction(fullClassName: String, functionName: String, project: Project) {
        // Şu anda fonksiyonu bilgilendirme amaçlı göster
        // Gerçek çalıştırma için ayrı bir mekanizma kurulacak
        Messages.showInfoMessage(
            project,
            "Ready to execute KMaestro function: '$functionName'\n" +
                "Class: $fullClassName\n\n" +
                "Note: Function execution will be implemented in future versions.\n" +
                "Currently showing function details for verification.",
            "KMaestro Function Detected"
        )
    }

    private fun getPackageName(file: PsiFile): String {
        val text = file.text
        val packagePattern = Regex("package\\s+([a-zA-Z_][a-zA-Z0-9_.]*)")
        val matchResult = packagePattern.find(text)
        return matchResult?.groupValues?.get(1) ?: ""
    }

    private fun getClassName(file: PsiFile): String {
        return file.name.substringBeforeLast(".kt") + "Kt"
    }

    private fun extractFunctionName(functionElement: PsiElement): String {
        val text = functionElement.text
        val funIndex = text.indexOf("fun ")
        if (funIndex != -1) {
            val afterFun = text.substring(funIndex + 4)
            val nameEnd = afterFun.indexOf('(')
            if (nameEnd != -1) {
                return afterFun.substring(0, nameEnd).trim()
            }
        }
        return "Unknown"
    }
}