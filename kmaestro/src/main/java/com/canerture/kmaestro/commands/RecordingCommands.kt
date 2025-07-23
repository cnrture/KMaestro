package com.canerture.kmaestro.commands

internal class RecordingCommands(private val commands: MutableList<String>) {

    /**
     * Starts recording the test session for video capture.
     * Useful for creating visual documentation of test runs or debugging failures.
     *
     * @param fileName The name of the recording file (default: "recording")
     */
    fun startRecording(fileName: String = "recording") =
        commands.add("- startRecording: \"$fileName\"")

    /**
     * Stops the current recording session.
     * Must be called after startRecording to finalize the video file.
     */
    fun stopRecording() = commands.add("- stopRecording")
}