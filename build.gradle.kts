plugins {
    java
}

group = "me.alexirving"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("com.konghq:unirest-java:3.11.09")
}
tasks{
jar{
    manifest {
        attributes["Main-Class"] = "me.alexirving.BellUpdater"
        archiveFileName.set("BellVote-${project.version}.jar")
    }
}
}
