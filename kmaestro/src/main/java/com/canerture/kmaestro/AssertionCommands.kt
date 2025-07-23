package com.canerture.kmaestro

internal class AssertionCommands(private val commands: MutableList<String>) {

    fun assertVisible(
        text: String? = null,
        id: String? = null,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focused: Boolean? = null,
        selected: Boolean? = null,
        index: Int? = null,
    ) {
        require(text != null || id != null) { "Either text or id must be provided." }
        val visibilityCommand = StringBuilder("- assertVisible:")

        if (text != null && id == null) {
            visibilityCommand.append(" \"$text\"")
        } else {
            visibilityCommand.append("\n")
            text?.let { visibilityCommand.append("    text: \"$it\"\n") }
            id?.let { visibilityCommand.append("    id: \"$it\"\n") }
            enabled?.let { visibilityCommand.append("    enabled: $it\n") }
            checked?.let { visibilityCommand.append("    checked: $it\n") }
            focused?.let { visibilityCommand.append("    focused: $it\n") }
            selected?.let { visibilityCommand.append("    selected: $it\n") }
            index?.let { visibilityCommand.append("    index: $it\n") }
        }

        commands.add(visibilityCommand.toString().trimEnd())
    }

    fun assertNotVisible(
        text: String? = null,
        id: String? = null,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focused: Boolean? = null,
        selected: Boolean? = null,
        index: Int? = null,
    ) {
        require(text != null || id != null) { "Either text or id must be provided." }
        val visibilityCommand = StringBuilder("- assertNotVisible:")

        if (text != null && id == null && enabled == null && checked == null &&
            focused == null && selected == null && index == null
        ) {
            visibilityCommand.append(" \"$text\"")
        } else {
            visibilityCommand.append("\n")
            text?.let { visibilityCommand.append("    text: \"$it\"\n") }
            id?.let { visibilityCommand.append("    id: \"$it\"\n") }
            enabled?.let { visibilityCommand.append("    enabled: $it\n") }
            checked?.let { visibilityCommand.append("    checked: $it\n") }
            focused?.let { visibilityCommand.append("    focused: $it\n") }
            selected?.let { visibilityCommand.append("    selected: $it\n") }
            index?.let { visibilityCommand.append("    index: $it\n") }
        }

        commands.add(visibilityCommand.toString().trimEnd())
    }

    fun assertTrue(condition: String) {
        require(condition.isNotEmpty()) { "Condition must not be empty." }
        commands.add("- assertTrue: \"$condition\"")
    }

    fun assertWithAI(description: String) {
        require(description.isNotEmpty()) { "Description must not be empty." }
        commands.add("- assertWithAI: \"$description\"")
    }

    fun assertNoDefectsWithAI() = commands.add("- assertNoDefectsWithAi")
}