package com.github.cnrture.kmaestro.gutter

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import java.awt.event.MouseEvent

class KMaestroGutterIconProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        // KMaestro fonksiyon çağrısı içeren fonksiyonları ara
        if (element is LeafPsiElement && element.text == "fun") {
            val functionElement = element.parent ?: return null

            // Fonksiyon içinde KMaestro çağrısı var mı kontrol et
            if (containsKMaestroCall(functionElement)) {
                return LineMarkerInfo(
                    element,
                    element.textRange,
                    AllIcons.Actions.Execute,
                    { "Run KMaestro Test" },
                    KMaestroActionHandler(functionElement),
                    GutterIconRenderer.Alignment.LEFT,
                    { "Run KMaestro Test" }
                )
            }
        }
        return null
    }

    private fun containsKMaestroCall(element: PsiElement): Boolean {
        return element.text.contains("KMaestro(")
    }

    private class KMaestroActionHandler(private val functionElement: PsiElement) :
        GutterIconNavigationHandler<PsiElement> {

        override fun navigate(e: MouseEvent?, element: PsiElement?) {
            val project = element?.project ?: return

            // Popup menü oluştur
            val popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(
                createActionPanel(functionElement, project), null
            )

            popupBuilder
                .setTitle("KMaestro Actions")
                .setResizable(false)
                .setMovable(true)
                .setRequestFocus(true)
                .createPopup()
                .showInBestPositionFor(element.containingFile.viewProvider.document?.let {
                    com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project)
                        .selectedTextEditor
                } ?: return)
        }

        private fun createActionPanel(
            functionElement: PsiElement,
            project: com.intellij.openapi.project.Project,
        ): javax.swing.JComponent {
            val panel = javax.swing.JPanel(java.awt.BorderLayout())
            val buttonPanel = javax.swing.JPanel(java.awt.GridLayout(0, 1, 5, 5))

            // Create YAML File butonu
            val createYamlButton = javax.swing.JButton("Create YAML File")
            createYamlButton.addActionListener {
                com.github.cnrture.kmaestro.actions.KMaestroActionHandler.createYamlFile(functionElement, project)
            }

            // Run Maestro Test butonu
            val runTestButton = javax.swing.JButton("Run Maestro Test")
            runTestButton.addActionListener {
                com.github.cnrture.kmaestro.actions.KMaestroActionHandler.runMaestroTest(functionElement, project)
            }

            buttonPanel.add(createYamlButton)
            buttonPanel.add(runTestButton)

            panel.add(buttonPanel, java.awt.BorderLayout.CENTER)
            panel.border = javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)

            return panel
        }
    }
}