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

    fun launchApp() = commands.add("- launchApp")

    fun addMedia(vararg path: String) {
        commands.add("- addMedia:")
        path.forEach { commands.add("    - \"$it\"") }
    }

    fun isVisibleText(
        text: String,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focuses: Boolean? = null,
        selected: Boolean? = null,
    ) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        val visibilityCommand = StringBuilder("- assertVisible:\n")
        if (text.isNotEmpty()) visibilityCommand.append("    text: \"$text\"\n")
        enabled?.let { visibilityCommand.append("    enabled: $it\n") }
        checked?.let { visibilityCommand.append("    checked: $it\n") }
        focuses?.let { visibilityCommand.append("    focused: $it\n") }
        selected?.let { visibilityCommand.append("    selected: $it\n") }
        commands.add(visibilityCommand.toString())
    }

    fun isVisibleTag(
        tag: String = "",
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focuses: Boolean? = null,
        selected: Boolean? = null,
    ) {
        require(tag.isNotEmpty()) { "Tag must not be empty." }
        val visibilityCommand = StringBuilder("- assertVisible:\n")
        if (tag.isNotEmpty()) visibilityCommand.append("    id: \"$tag\"\n")
        enabled?.let { visibilityCommand.append("    enabled: $it\n") }
        checked?.let { visibilityCommand.append("    checked: $it\n") }
        focuses?.let { visibilityCommand.append("    focused: $it\n") }
        selected?.let { visibilityCommand.append("    selected: $it\n") }
        commands.add(visibilityCommand.toString())
    }

    fun isInvisibleText(
        text: String,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focuses: Boolean? = null,
        selected: Boolean? = null,
    ) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        val visibilityCommand = StringBuilder("- assertNotVisible:\n")
        if (text.isNotEmpty()) visibilityCommand.append("    text: \"$text\"\n")
        enabled?.let { visibilityCommand.append("    enabled: $it\n") }
        checked?.let { visibilityCommand.append("    checked: $it\n") }
        focuses?.let { visibilityCommand.append("    focused: $it\n") }
        selected?.let { visibilityCommand.append("    selected: $it\n") }
        commands.add(visibilityCommand.toString())
    }

    fun isInvisibleTag(
        tag: String = "",
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focuses: Boolean? = null,
        selected: Boolean? = null,
    ) {
        require(tag.isNotEmpty()) { "Tag must not be empty." }
        val visibilityCommand = StringBuilder("- assertNotVisible:\n")
        if (tag.isNotEmpty()) visibilityCommand.append("    id: \"$tag\"\n")
        enabled?.let { visibilityCommand.append("    enabled: $it\n") }
        checked?.let { visibilityCommand.append("    checked: $it\n") }
        focuses?.let { visibilityCommand.append("    focused: $it\n") }
        selected?.let { visibilityCommand.append("    selected: $it\n") }
        commands.add(visibilityCommand.toString())
    }

    fun isTrue(condition: String) {
        require(condition.isNotEmpty()) { "Condition must not be empty." }
        commands.add("- assertTrue: \"$condition\"")
    }

    fun back() = commands.add("- back")

    fun clearState(appId: String = "") {
        if (appId.isEmpty()) commands.add("- clearState") else commands.add("- clearState: $appId")
    }

    fun clickText(text: String) = commands.add("- tapOn: \"$text\"")

    fun clickTag(tag: String) = commands.add("- tapOn:\n    id: \"$tag\"")

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