package com.canerture.kmaestro

internal class InputCommands(private val commands: MutableList<String>) {

    /**
     * Inputs text into the currently focused text field.
     * The text field must be focused before calling this method.
     *
     * @param text The text to input
     * @throws IllegalArgumentException if text is empty
     */
    fun inputText(text: String) {
        require(text.isNotEmpty()) { "Text must not be empty." }
        commands.add("- inputText: \"$text\"")
    }

    /**
     * Inputs a randomly generated email address.
     * Useful for testing registration flows or any feature requiring unique emails.
     * Format: random-string@example.com
     */
    fun inputRandomEmail() = commands.add("- inputRandomEmail")

    /**
     * Inputs a randomly generated person name.
     * Useful for testing forms that require names.
     * Generates both first and last names.
     */
    fun inputRandomPersonName() = commands.add("- inputRandomPersonName")

    /**
     * Inputs a randomly generated number with specified length.
     * Generates numeric digits only.
     *
     * @param length The number of digits to generate (default: 8)
     */
    fun inputRandomNumber(length: Int = 8) =
        commands.add("- inputRandomNumber:\n    length: $length")

    /**
     * Inputs randomly generated text with specified length.
     * Generates alphanumeric characters.
     *
     * @param length The number of characters to generate (default: 8)
     */
    fun inputRandomText(length: Int = 8) =
        commands.add("- inputRandomText:\n    length: $length")

    /**
     * Erases text from the currently focused text field.
     * Can erase specific number of characters or all text.
     *
     * @param charactersToErase The number of characters to erase. If null, erases all text
     */
    fun eraseText(charactersToErase: Int? = null) {
        if (charactersToErase == null) {
            commands.add("- eraseText")
        } else {
            commands.add("- eraseText: $charactersToErase")
        }
    }

    /**
     * Hides the on-screen keyboard if it's currently visible.
     * Useful after text input to clear the screen view.
     */
    fun hideKeyboard() = commands.add("- hideKeyboard")

    /**
     * Copies text from a UI element to the clipboard.
     * Element can be identified by text content or ID.
     *
     * @param text The text content of the element to copy from
     * @param id The accessibility ID or resource ID of the element to copy from
     * @throws IllegalArgumentException if neither text nor id is provided
     */
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

    /**
     * Pastes text from the clipboard into the currently focused text field.
     * Use this after copyTextFrom to transfer text between fields.
     */
    fun pasteText() = commands.add("- pasteText")
}