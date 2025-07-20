package com.canerture.kmaestro

import java.io.File

class KMaestro(
    private val appId: String,
    private val path: String,
    private val yamlName: String,
) {

    private val commands = mutableListOf<String>()

    init {
        commands.add("# $yamlName\n")
        commands.add("- appId: $appId\n---")
    }

    fun addMedia(vararg path: String) {
        commands.add("- addMedia:")
        path.forEach { commands.add("    - \"$it\"") }
    }
    fun launchApp() = commands.add("- launchApp")
    fun clickText(text: String) = commands.add("- tapOn: \"$text\"")
    fun clickId(id: String) = commands.add("- tapOn:\n    id: \"$id\"")

    fun build(): String {
        val yaml = commands.joinToString("\n")
        val directory = File(path)
        if (!directory.exists()) directory.mkdirs()
        val fileName = if (yamlName.endsWith(".yaml")) yamlName else "$yamlName.yaml"
        val file = File(directory, fileName)
        file.writeText(yaml)
        commands.clear()
        return yaml
    }
}