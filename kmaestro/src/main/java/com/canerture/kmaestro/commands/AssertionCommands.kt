package com.canerture.kmaestro.commands

internal class AssertionCommands(private val commands: MutableList<String>) {

    /**
     * Asserts that a UI element is visible on the screen.
     * This command will wait for the element to appear if it's not immediately visible.
     *
     * @param text The text content to look for in the element
     * @param id The accessibility ID or resource ID of the element
     * @param enabled Whether the element should be enabled (true/false/null for any)
     * @param checked Whether the element should be checked (true/false/null for any)
     * @param focused Whether the element should have keyboard focus (true/false/null for any)
     * @param selected Whether the element should be selected (true/false/null for any)
     * @param index The index of the element if multiple elements match (0-based)
     * @throws IllegalArgumentException if neither text nor id is provided
     */
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

    /**
     * Asserts that a UI element is NOT visible on the screen.
     * This command will wait for the element to disappear if it's currently visible.
     *
     * @param text The text content to look for in the element
     * @param id The accessibility ID or resource ID of the element
     * @param enabled Whether the element should be enabled (true/false/null for any)
     * @param checked Whether the element should be checked (true/false/null for any)
     * @param focused Whether the element should have keyboard focus (true/false/null for any)
     * @param selected Whether the element should be selected (true/false/null for any)
     * @param index The index of the element if multiple elements match (0-based)
     * @throws IllegalArgumentException if neither text nor id is provided
     */
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

    /**
     * Asserts that a JavaScript condition evaluates to true.
     * Useful for custom validations that can't be expressed with standard assertions.
     *
     * @param condition A JavaScript expression that should evaluate to true
     * @throws IllegalArgumentException if condition is empty
     */
    fun assertTrue(condition: String) {
        require(condition.isNotEmpty()) { "Condition must not be empty." }
        commands.add("- assertTrue: \"$condition\"")
    }

    /**
     * Uses AI to assert that a certain condition or state is present in the app.
     * This leverages Maestro's AI capabilities for intelligent assertions.
     *
     * @param description A natural language description of what should be asserted
     * @throws IllegalArgumentException if description is empty
     */
    fun assertWithAI(description: String) {
        require(description.isNotEmpty()) { "Description must not be empty." }
        commands.add("- assertWithAI: \"$description\"")
    }

    /**
     * Uses AI to detect any visual defects or issues in the current screen.
     * This is useful for automated UI quality checks and detecting visual regressions.
     */
    fun assertNoDefectsWithAI() = commands.add("- assertNoDefectsWithAi")
}