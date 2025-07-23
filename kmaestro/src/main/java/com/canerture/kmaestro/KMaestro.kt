package com.canerture.kmaestro

import java.io.File

/**
 * KMaestro - Kotlin DSL for Maestro mobile testing framework
 *
 * A type-safe, fluent API for creating Maestro YAML test flows in Kotlin.
 * Provides comprehensive coverage of all Maestro commands with enhanced developer experience.
 *
 * @param path The directory path where the YAML file will be saved
 * @param yamlName The name of the YAML file (without .yaml extension)
 *
 * @author KMaestro Team
 * @since 1.0.0
 */
class KMaestro(
    private val path: String,
    private val yamlName: String,
    config: (KMaestro.() -> Unit)? = null
) {

    private var commands = mutableListOf<String>()

    // Command builders
    private val appCommands = AppCommands(commands)
    private val inputCommands = InputCommands(commands)
    private val interactionCommands = InteractionCommands(commands)
    private val assertionCommands = AssertionCommands(commands)
    private val deviceCommands = DeviceCommands(commands)
    private val waitCommands = WaitCommands(commands)
    private val scriptCommands = ScriptCommands(commands)
    private val recordingCommands = RecordingCommands(commands)

    init {
        commands.add("# $yamlName\n")
        config?.invoke(this)
        build()
    }

    // App-related commands delegation
    fun launchApp(
        appId: String? = null,
        clearState: Boolean = false,
        clearKeychain: Boolean = false,
        stopApp: Boolean = true,
        permissions: Map<String, String>? = null,
        arguments: Map<String, Any> = emptyMap(),
    ) = appCommands.launchApp(appId, clearState, clearKeychain, stopApp, permissions, arguments)

    fun killApp(appId: String? = null) = appCommands.killApp(appId)
    fun stopApp(appId: String? = null) = appCommands.stopApp(appId)
    fun clearState(appId: String? = null) = appCommands.clearState(appId)
    fun clearKeychain() = appCommands.clearKeychain()

    // Input-related commands delegation
    fun inputText(text: String) = inputCommands.inputText(text)
    fun inputRandomEmail() = inputCommands.inputRandomEmail()
    fun inputRandomPersonName() = inputCommands.inputRandomPersonName()
    fun inputRandomNumber(length: Int = 8) = inputCommands.inputRandomNumber(length)
    fun inputRandomText(length: Int = 8) = inputCommands.inputRandomText(length)
    fun eraseText(charactersToErase: Int? = null) = inputCommands.eraseText(charactersToErase)
    fun hideKeyboard() = inputCommands.hideKeyboard()
    fun copyTextFrom(text: String? = null, id: String? = null) =
        inputCommands.copyTextFrom(text, id)

    fun pasteText() = inputCommands.pasteText()

    // Interaction commands delegation
    fun tapOn(
        text: String? = null,
        id: String? = null,
        point: String? = null,
        index: Int? = null,
        repeat: Int = 1,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null,
        longPressOn: Boolean = false
    ) = interactionCommands.tapOn(
        text,
        id,
        point,
        index,
        repeat,
        delay,
        retryTapIfNoChange,
        waitToSettleTimeoutMs,
        longPressOn
    )

    fun doubleTapOn(
        text: String? = null,
        id: String? = null,
        point: String? = null,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null
    ) = interactionCommands.doubleTapOn(
        text,
        id,
        point,
        delay,
        retryTapIfNoChange,
        waitToSettleTimeoutMs
    )

    fun longPressOn(
        text: String? = null,
        id: String? = null,
        point: String? = null,
        repeat: Int = 1,
        delay: Int = 100,
        retryTapIfNoChange: Boolean = false,
        waitToSettleTimeoutMs: Int? = null
    ) = interactionCommands.longPressOn(
        text,
        id,
        point,
        repeat,
        delay,
        retryTapIfNoChange,
        waitToSettleTimeoutMs
    )

    fun swipe(
        startX: String? = null,
        startY: String? = null,
        endX: String? = null,
        endY: String? = null,
        direction: Direction? = null,
        from: Map<String, String>? = null,
        duration: Int = 400
    ) = interactionCommands.swipe(startX, startY, endX, endY, direction, from, duration)

    fun scroll(direction: Direction = Direction.DOWN) = interactionCommands.scroll(direction)
    fun scrollUntilVisible(
        text: String? = null,
        id: String? = null,
        direction: Direction = Direction.DOWN,
        timeout: Int = 20000,
        speed: Int = 40,
        visibilityPercentage: Int = 100,
        centerElement: Boolean = false,
    ) = interactionCommands.scrollUntilVisible(
        text,
        id,
        direction,
        timeout,
        speed,
        visibilityPercentage,
        centerElement
    )

    fun back() = interactionCommands.back()

    // Assertion commands delegation
    fun assertVisible(
        text: String? = null,
        id: String? = null,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focused: Boolean? = null,
        selected: Boolean? = null,
        index: Int? = null,
    ) = assertionCommands.assertVisible(text, id, enabled, checked, focused, selected, index)

    fun assertNotVisible(
        text: String? = null,
        id: String? = null,
        enabled: Boolean? = null,
        checked: Boolean? = null,
        focused: Boolean? = null,
        selected: Boolean? = null,
        index: Int? = null,
    ) = assertionCommands.assertNotVisible(text, id, enabled, checked, focused, selected, index)

    fun assertTrue(condition: String) = assertionCommands.assertTrue(condition)
    fun assertWithAI(description: String) = assertionCommands.assertWithAI(description)
    fun assertNoDefectsWithAI() = assertionCommands.assertNoDefectsWithAI()

    // Device commands delegation
    fun setAirplaneMode(enabled: Boolean) = deviceCommands.setAirplaneMode(enabled)
    fun toggleAirplaneMode() = deviceCommands.toggleAirplaneMode()
    fun setLocation(latitude: Double, longitude: Double) =
        deviceCommands.setLocation(latitude, longitude)

    fun travel(points: List<Pair<Double, Double>>, speed: Int = 7900) =
        deviceCommands.travel(points, speed)

    fun pressKey(key: KeyType) = deviceCommands.pressKey(key)
    fun addMedia(vararg path: String) = deviceCommands.addMedia(*path)
    fun openLink(url: String, autoVerify: Boolean? = null, browser: Boolean? = null) =
        deviceCommands.openLink(url, autoVerify, browser)

    fun takeScreenshot(fileName: String = "screenshot") = deviceCommands.takeScreenshot(fileName)

    // Wait commands delegation
    fun waitForAnimationToEnd(timeout: Int = 5000) = waitCommands.waitForAnimationToEnd(timeout)
    fun extendedWaitUntil(
        visible: Map<String, Any>? = null,
        notVisible: Map<String, Any>? = null,
        timeout: Int = 10000
    ) = waitCommands.extendedWaitUntil(visible, notVisible, timeout)

    // Script commands delegation
    fun evalScript(script: String) = scriptCommands.evalScript(script)
    fun runScript(script: String, env: Map<String, String>? = null) =
        scriptCommands.runScript(script, env)

    fun runFlow(flowName: String) = scriptCommands.runFlow(flowName)
    fun extractTextWithAI(description: String): String =
        scriptCommands.extractTextWithAI(description)

    // Recording commands delegation
    fun startRecording(fileName: String = "recording") = recordingCommands.startRecording(fileName)
    fun stopRecording() = recordingCommands.stopRecording()

    // Control flow commands
    fun repeat(times: Int, commands: List<String>) {
        this.commands.add("- repeat:")
        this.commands.add("    times: $times")
        this.commands.add("    commands:")
        commands.forEach { command ->
            this.commands.add("      $command")
        }
    }

    fun retry(maxRetries: Int = 3, commands: List<String>) {
        this.commands.add("- retry:")
        this.commands.add("    maxRetries: $maxRetries")
        this.commands.add("    commands:")
        commands.forEach { command ->
            this.commands.add("      $command")
        }
    }

    internal fun build(): String {
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