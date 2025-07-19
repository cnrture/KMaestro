package com.canerture.kmaestro

class KMaestro(
    private val appId: String,
    private val path: String,
    private val yamlName: String,
) {

    private val commands = mutableListOf<String>()
    private var currentAppId: String? = null

    init {
        appId(appId)
    }

    fun appId(appId: String): KMaestro {
        currentAppId = appId
        commands.add("- appId: $appId")
        return this
    }

    fun launchApp(): KMaestro {
        commands.add("- launchApp")
        return this
    }

    fun click(target: String): KMaestro {
        commands.add("- tapOn: \"$target\"")
        return this
    }

    fun build(): String {
        val yaml = commands.joinToString("\n")
        commands.clear()
        currentAppId = null
        return yaml
    }
}