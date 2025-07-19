package com.canerture.kmaestro

fun main() {
    KMaestro(
        appId = "com.canerture.kmaestro",
        path = "maestro",
        yamlName = "sample.yaml"
    ).apply {
        launchApp()
        clickText("button1")
        clickId("button2")
        build()
    }
}