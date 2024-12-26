pluginManagement.repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.neoforged.net/releases/")
    maven("https://jitpack.io")
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    id("com.gradle.develocity") version("3.18.1")
}

develocity.buildScan {
    termsOfUseUrl = "https://gradle.com/terms-of-service"
    termsOfUseAgree = "yes"
}

include("common")
include("fabric")
include("neoforge")

rootProject.name = "FrozenBlock Multiloader Template"

localRepository("FrozenLib-Multiloader", true, true, true)

// TODO: [Treetrain1] make this support ML FLib modules. This cannot detect sub-projects/modules for some reason.
fun localRepository(repo: String, kotlin: Boolean, enabled: Boolean, multiloader: Boolean = false) {
    if (!enabled) {
        println("Not including local repo $repo as it is disabled")
        return
    }
    println("Attempting to include local repo $repo")

    val github = System.getenv("GITHUB_ACTIONS") == "true"

    val allowLocalRepoUse = true
    val allowLocalRepoInConsoleMode = true

    val androidInjectedInvokedFromIde by extra("android.injected.invoked.from.ide")
    val xpcServiceName by extra("XPC_SERVICE_NAME")
    val ideaInitialDirectory by extra("IDEA_INITIAL_DIRECTORY")

    val isIDE = androidInjectedInvokedFromIde != "" || (System.getenv(xpcServiceName) ?: "").contains("intellij") || (System.getenv(xpcServiceName) ?: "").contains(".idea") || System.getenv(ideaInitialDirectory) != null

    var path = "../$repo"
    var file = File(path)

    val prefixedRepoName = ":$repo"

    if (allowLocalRepoUse && (isIDE || allowLocalRepoInConsoleMode)) {
        if (github) {
            path = repo
            file = File(path)
            println("Running on GitHub")
        }
        if (file.exists()) {
            include(prefixedRepoName)
            project(prefixedRepoName).projectDir = file
            project(prefixedRepoName).buildFileName = "./build.gradle" + if (kotlin) ".kts" else ""
            println("Included local repo $repo")
        } else {
            error("Local repo $repo not found")
        }
    }

    // TODO: [Treetrain1] make this support ML FLib modules. This cannot detect sub-projects/modules for some reason.
    if (multiloader) {
        localRepository("$repo:common", kotlin, true, false)
        localRepository("$repo:fabric", kotlin, true, false)
        localRepository("$repo:neoforge", kotlin, true, false)
    }
}
