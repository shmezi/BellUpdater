plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
}