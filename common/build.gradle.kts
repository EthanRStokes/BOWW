plugins {
    id("com.github.johnrengelman.shadow") version("+")
    id("maven-publish")
}

architectury {
    common("fabric")
    common("neoforge")
    platformSetupLoomIde()
}

val mod_id: String by project
val fabric_loader_version: String by project

loom.accessWidenerPath.set(file("src/main/resources/${mod_id}.accesswidener"))

sourceSets.main.get().resources.srcDir("src/main/generated/resources")

val frozenlib_version: String by project
val build_with_frozenlib: String by project
val local_frozenlib = findProject(":FrozenLib-Multiloader") != null

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")

    if (local_frozenlib) {
        api(project(":FrozenLib-Multiloader", configuration = "namedElements"))
    } else {
        implementation("maven.modrinth:frozenlib:$frozenlib_version-fabric")
    }
}
