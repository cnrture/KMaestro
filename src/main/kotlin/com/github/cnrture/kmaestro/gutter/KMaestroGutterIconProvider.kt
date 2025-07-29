package com.github.cnrture.kmaestro.gutter

import com.github.cnrture.kmaestro.services.MaestroService
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import java.awt.BorderLayout
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

class KMaestroGutterIconProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element is LeafPsiElement && element.text == "fun") {
            val functionElement = element.parent ?: return null
            if (containsKMaestroCall(functionElement)) {
                return LineMarkerInfo(
                    element,
                    element.textRange,
                    AllIcons.Actions.Execute,
                    { "Run KMaestro Test" },
                    KMaestroActions(functionElement),
                    GutterIconRenderer.Alignment.LEFT,
                    { "Run KMaestro Test" }
                )
            }
        }
        return null
    }

    private fun containsKMaestroCall(element: PsiElement) = element.text.contains("KMaestro(")

    private class KMaestroActions(private val functionElement: PsiElement) :
        GutterIconNavigationHandler<PsiElement> {

        override fun navigate(e: MouseEvent?, element: PsiElement?) {
            val project = element?.project ?: return

            val popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(
                createActionPanel(functionElement, project), null
            )

            popupBuilder
                .setTitle("KMaestro Actions")
                .setResizable(false)
                .setRequestFocus(true)
                .createPopup()
                .showInBestPositionFor(element.containingFile.viewProvider.document?.let {
                    FileEditorManager.getInstance(project).selectedTextEditor
                } ?: return)
        }

        private fun createActionPanel(
            functionElement: PsiElement,
            project: Project,
        ): JComponent {
            val maestroService = project.service<MaestroService>()
            val panel = JPanel(BorderLayout())
            val buttonPanel = JPanel(java.awt.GridLayout(0, 1, 5, 5))

            val createYamlButton = JButton("Create YAML File")
            createYamlButton.addActionListener {
                // This is a placeholder for the actual implementation
            }

            val runTestButton = JButton("Run Maestro Test")
            runTestButton.addActionListener {
                val lines = functionElement.text.split("\n")
                val path = lines.find { it.contains("path") }?.substringAfter("path = ")?.trim()?.trimEnd('"')?.trimEnd(',')?.replace("\"", "") ?: return@addActionListener
                val yamlName = lines.find { it.contains("yamlName") }?.substringAfter("yamlName = ")?.trim()?.trimEnd('"')?.trimEnd(',')?.replace("\"", "") ?: return@addActionListener
                println(path)
                println(yamlName)
                maestroService.runMaestroTest("$path/$yamlName.yaml").thenAccept { result ->
                    val message = if (result.success) {
                        "Test completed successfully:\n${result.output}"
                    } else {
                        "Test failed with exit code ${result.exitCode}:\n${result.output}"
                    }
                    println("Maestro Test Result: $message")
                    JBPopupFactory.getInstance().createMessage(message).showInFocusCenter()
                }
            }

            buttonPanel.add(createYamlButton)
            buttonPanel.add(runTestButton)

            panel.add(buttonPanel, BorderLayout.CENTER)
            panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            return panel
        }
    }
}