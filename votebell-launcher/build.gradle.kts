plugins {
    id("bellvotes-library-conventions")
    application
}
dependencies {
    implementation(project(":votebell-common"))
}
application {
    mainClass.set("me.alexirving.launcher.LauncherKt")
}