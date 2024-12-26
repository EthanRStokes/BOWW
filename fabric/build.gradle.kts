plugins {
    id("com.github.johnrengelman.shadow") version("+")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

configurations {
    create("common")
    "common" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentFabric").extendsFrom(configurations["common"])
}

loom.accessWidenerPath.set(project(":common").loom.accessWidenerPath)

// Fabric Datagen Gradle config. Remove if not using Fabric datagen
fabricApi.configureDataGeneration()

val mod_id: String by project
val mod_version: String by project
val mod_name: String by project
val mod_author: String by project
val mod_description: String by project
val credits: String by project
val mod_license: String by project
val java_version: String by project
val minecraft_version: String by project
val parchment_version: String by project
val fabric_loader_version: String by project
val fabric_api_version: String by project
val minecraft_version_range_fabric: String by project
val neoforge_version: String by project
val minecraft_version_range_neoforge: String by project

val frozenlib_version: String by project
val build_with_frozenlib: String by project
val local_frozenlib = findProject(":FrozenLib-Multiloader") != null

val cloth_config_version: String by project
val modmenu_version: String by project

repositories {
    maven("https://maven.terraformersmc.com")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.minecraftforge.net/")
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")
    modApi("net.fabricmc.fabric-api:fabric-api:${fabric_api_version}+$minecraft_version")

    if (local_frozenlib) {
        api(project(":FrozenLib-Multiloader", configuration = "namedElements"))
    } else {
        implementation("maven.modrinth:frozenlib:$frozenlib_version-fabric")
    }

    // TODO: [Treetrain1] Figure out how to allow for FLib to be included in builds
    /*
    if (local_frozenlib) {
        if (build_with_frozenlib == "true") {
            modCompileOnly(project(":FrozenLib-Multiloader"))?.let { include(it) }
        }
    } else {
        if (build_with_frozenlib == "true") {
            modApi("maven.modrinth:frozenlib:$frozenlib_version-fabric")?.let { include(it) }
        } else {
            modApi("maven.modrinth:frozenlib:$frozenlib_version-fabric")
        }
    }
     */

    modApi("me.shedaniel.cloth:cloth-config-fabric:${cloth_config_version}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi("com.terraformersmc:modmenu:${modmenu_version}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionFabric"))
}

tasks {
    shadowJar {
        exclude("architectury.common.json")

        configurations = listOf(project.configurations.getByName("shadowBundle"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }
}
