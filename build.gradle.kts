plugins {
    java
}

/*
Setup environment variables
* stsInstallLocation should point to the steam install directory
* compileOnlyLibs should point to a directory containing any JARs you reference
    - e.g. this directory should have desktop-1.0.jar, ModTheSpire.jar, BaseMod.jar
    - NOTE: these compileOnlyLibs are not included in the JAR, so you will get runtime
      errors if you try and call code that won't exist on the client.
 */
var stsInstallLocation: String = System.getenv("STS_INSTALL")
var compileOnlyLibs: String = System.getenv("STS_MODDING_LIB")

// Uses the value written in settings.gradle
var modName: String = rootProject.name

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(fileTree(compileOnlyLibs))
    //implementation("io.github.casey-c:easel:0.0.2")
}


tasks.register<Jar>("buildJAR") {
    group = "Slay the Spire"
    description = "Builds a fat (includes runtime dependencies) JAR in the build/libs folder"

    // Main code
    from(sourceSets.main.get().output)

    // Any runtime dependencies (e.g. from mavenCentral(), local JARs, etc.)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter {
            it.name.endsWith("jar")
        }.map {
            zipTree(it)
        }
    })
}

tasks.register<Copy>("buildAndCopyJAR") {
    group = "Slay the Spire"
    description = "Copies the JAR from your build/libs folder into your \$STS_INSTALL location"

    dependsOn("buildJAR")

    from("build/libs/$modName.jar")
    into("$stsInstallLocation\\mods")
}
