
rootProject.name = "BellVotes"
include("votebell-launcher","votebell-app","votebell-common")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}