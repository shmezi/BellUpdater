plugins {
    java
}

group = "me.alexirving"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
}
tasks{
jar{
    manifest {
        attributes["Main-Class"] = "me.alexirving.BellUpdater"
    }
}
}
