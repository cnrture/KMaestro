package com.canerture.kmaestro

internal class RecordingCommands(private val commands: MutableList<String>) {

    fun startRecording(fileName: String = "recording") =
        commands.add("- startRecording: \"$fileName\"")

    fun stopRecording() = commands.add("- stopRecording")
}