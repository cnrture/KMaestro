package com.canerture.kmaestro

internal class InputCommands(private val commands: MutableList<String>) {

    fun inputText(text: String) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        commands.add("- inputText: \"$text\"")
    }

    fun inputRandomEmail() = commands.add("- inputRandomEmail")

    fun inputRandomPersonName() = commands.add("- inputRandomPersonName")

    fun inputRandomNumber(length: Int = 8) =
        commands.add("- inputRandomNumber:\n    length: $length")

    fun inputRandomText(length: Int = 8) =
        commands.add("- inputRandomText:\n    length: $length")

    fun eraseText(charactersToErase: Int? = null) {
        if (charactersToErase == null) {
            commands.add("- eraseText")
        } else {
            commands.add("- eraseText: $charactersToErase")
        }
    }

    fun hideKeyboard() = commands.add("- hideKeyboard")

    fun copyTextFrom(text: String? = null, id: String? = null) {
        require(text != null || id != null) { "Either text or id must be provided." }
        val copyCommand = StringBuilder("- copyTextFrom:")

        if (text != null && id == null) {
            copyCommand.append(" \"$text\"")
        } else {
            copyCommand.append("\n")
            text?.let { copyCommand.append("    text: \"$it\"\n") }
            id?.let { copyCommand.append("    id: \"$it\"\n") }
        }

        commands.add(copyCommand.toString().trimEnd())
    }

    fun pasteText() = commands.add("- pasteText")
}