package com.canerture.kmaestro

fun main() {
    KMaestro(
        path = "maestro",
        yamlName = "sample.yaml"
    ).apply {
        launchApp("com.canerture.kmaestro")
        build()
    }
}