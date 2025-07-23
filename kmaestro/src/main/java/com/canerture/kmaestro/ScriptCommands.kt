package com.canerture.kmaestro

internal class ScriptCommands(private val commands: MutableList<String>) {

    fun evalScript(script: String) {
        require(script.isNotEmpty()) { "Script must not be empty." }
        commands.add("- evalScript: \"$script\"")
    }

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

    fun runFlow(flowName: String) {
        require(flowName.isNotEmpty()) { "Flow name must not be empty." }
        commands.add("- runFlow: \"$flowName\"")
    }

    fun extractTextWithAI(description: String): String {
        require(description.isNotEmpty()) { "Description must not be empty." }
        commands.add("- extractTextWithAI: \"$description\"")
        return "\${output.extractedText}"
    }
}