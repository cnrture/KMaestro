package com.canerture.kmaestro

fun main() {
    KMaestro(
        appId = "com.canerture.kmaestro",
        path = "maestro",
        yamlName = "sample.yaml"
    ).apply {
        launchApp()
        build()
    }
}