plugins {
    java
}

/* --------------------------------------------------------------------------------
Setup environment variables
  * stsInstallLocation should point to the steam install directory
  * compileOnlyLibs should point to a directory containing any JARs you reference
      - e.g. this directory should have desktop-1.0.jar, ModTheSpire.jar,
        BaseMod.jar
      - NOTE: these compileOnlyLibs are not included in the JAR, so you will get
        runtime errors if you try and call code that won't exist on the client.
 -------------------------------------------------------------------------------- */
var stsInstallLocation: String = System.getenv("STS_INSTALL")
var compileOnlyLibs: String = System.getenv("STS_MODDING_LIB")

// Uses the value written in settings.gradle
var modName: String = rootProject.name

// --------------------------------------------------------------------------------

/*
// If you use a jar which can be dynamically obtained from mavenCentral or jitpack
// etc., be sure to declare the proper source here before referring to it
// specifically by name in the dependencies section.
repositories {
    mavenCentral()
}
*/

// NOTE:
//  * compileOnly means we can refer to it but it won't be copied to the "fat" JAR
//  * implementation means we copy it to the fat JAR and have it available for
//    clients to use
//
// e.g. BaseMod/ModTheSpire are compileOnly because they will exist when the game
//   loads and we don't need to include them twice. If you pull a random library
//   from github or something, you'll need to bake it into the final JAR (making it
//   fat) in order to use it in game without runtime errors. Declaring these libs
//   implementation will automatically bake them into the final JAR using our build
//   tasks.
dependencies {
    compileOnly(fileTree(compileOnlyLibs))

    // Add extra library JARs here (e.g. from mavenCentral, jitpack, local, etc.).
    // The database repo versions (like jitpack) typically have instructions on
    // what to copy into here - so refer to those as needed.
    // Examples:
    //implementation("name:of:maven:library")
    //implementation(file("/path/to/local/jar"))
}

/* --------------------------------------------------------------------------------
Tasks to build our JARs
  (you shouldn't have to edit anything below)
 -------------------------------------------------------------------------------- */

tasks.register<Jar>("buildJAR") {
    group = "Slay the Spire"
    description = "Builds a fat JAR (includes runtime dependencies) in the build/libs folder"

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
    description = "Builds and copies a fat JAR to your \$STS_INSTALL/mods folder"

    dependsOn("buildJAR")

    from("build/libs/$modName.jar")
    into("$stsInstallLocation\\mods")
}
