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
        appId(appId)
    }

    fun appId(appId: String) = commands.add("- appId: $appId")
    fun launchApp() = commands.add("- launchApp")
    fun click(target: String) = commands.add("- tapOn: \"$target\"")

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