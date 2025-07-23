package com.canerture.kmaestro

internal class ScriptCommands(private val commands: MutableList<String>) {

    /**
     * Evaluates a JavaScript script in the context of the app.
     * Useful for custom logic, data manipulation, or complex validations.
     *
     * @param script The JavaScript code to execute
     * @throws IllegalArgumentException if script is empty
     */
    fun evalScript(script: String) {
        require(script.isNotEmpty()) { "Script must not be empty." }
        commands.add("- evalScript: \"$script\"")
    }

    /**
     * Runs a JavaScript script with optional environment variables.
     * Can execute script files or inline script content with custom environment.
     *
     * @param script The script content or file path to run
     * @param env Optional environment variables to pass to the script
     * @throws IllegalArgumentException if script is empty
     */
    fun runScript(script: String, env: Map<String, String>? = null) {
        require(script.isNotEmpty()) { "Script must not be empty." }
        val scriptCommand = StringBuilder("- runScript:")

        if (env == null) {
            scriptCommand.append(" \"$script\"")
        } else {
            scriptCommand.append("\n    script: \"$script\"")
            if (env.isNotEmpty()) {
                scriptCommand.append("\n    env:")
                env.forEach { (key, value) ->
                    scriptCommand.append("\n      $key: \"$value\"")
                }
            }
        }

        commands.add(scriptCommand.toString())
    }

    /**
     * Runs a predefined flow by name. Flows are reusable command sequences.
     * Useful for modular test design and code reuse.
     *
     * @param flowName The name of the flow to run
     * @throws IllegalArgumentException if flowName is empty
     */
    fun runFlow(flowName: String) {
        require(flowName.isNotEmpty()) { "Flow name must not be empty." }
        commands.add("- runFlow: \"$flowName\"")
    }

    /**
     * Uses AI to extract text from the screen based on a natural language description.
     * Leverages Maestro's AI capabilities for intelligent text extraction.
     *
     * @param description A description of what text to extract
     * @return A placeholder string that can be used to reference the extracted text
     * @throws IllegalArgumentException if description is empty
     */
    fun extractTextWithAI(description: String): String {
        require(description.isNotEmpty()) { "Description must not be empty." }
        commands.add("- extractTextWithAI: \"$description\"")
        return "\${output.extractedText}"
    }
}