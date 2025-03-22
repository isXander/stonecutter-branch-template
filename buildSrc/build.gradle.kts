plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.kikugie.dev/snapshots")
}

dependencies {
    fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"

    implementation(plugin("dev.kikugie.stonecutter", "0.6-alpha.11"))
    implementation(plugin("me.modmuss50.mod-publish-plugin", "0.8.4"))
}