plugins {
    id("com.github.johnrengelman.shadow") version("+")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

configurations {
    create("common")
    "common" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

/*
loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    // NeoForge Datagen Gradle config.  Remove if not using NeoForge datagen
    runs.create("datagen") {
        data()
        programArgs("--all", "--mod", "frozenlib")
        programArgs("--output", project(":common").file("src/main/generated/resources").absolutePath)
        programArgs("--existing", project(":common").file("src/main/resources").absolutePath)
    }
}
 */

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

dependencies {
    neoForge("net.neoforged:neoforge:${neoforge_version}")

    if (local_frozenlib) {
        api(project(":FrozenLib-Multiloader", configuration = "namedElements"))
    } else {
        implementation("maven.modrinth:frozenlib:$frozenlib_version-neoforge")
    }

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionNeoForge"))
}
tasks {
    shadowJar {
        exclude("architectury.common.json")

        configurations = listOf(project.configurations.getByName("shadowBundle"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        atAccessWideners.add("${mod_id}.accesswidener")
    }
}
