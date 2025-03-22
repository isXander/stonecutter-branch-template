plugins {
    id("multiloader-common")
    id("me.modmuss50.mod-publish-plugin")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = true
}
val commonResources: Configuration by configurations.creating {
    isCanBeResolved = true
}

dependencies {
    val commonPath = common.hierarchy.toString()
    compileOnly(project(path = commonPath)) {
        capabilities {
            requireCapability("$group:${mod.id}")
        }
    }
    commonJava(project(path = commonPath, configuration = "commonJava"))
    commonResources(project(path = commonPath, configuration = "commonResources"))
}

tasks {
    compileJava {
        dependsOn(commonJava)
        source(commonJava)
    }

    processResources {
        dependsOn(commonResources)
        from(commonResources)
    }

    javadoc {
        dependsOn(commonJava)
        source(commonJava)
    }

    named<Jar>("sourcesJar") {
        dependsOn(commonJava)
        from(commonJava)

        dependsOn(commonResources)
        from(commonResources)
    }
}

publishMods {
    displayName = "${mod.version} for $loader ${commonMod.mc}"
    version = project.version.toString()
    changelog = rootProject.file("changelog.md").readText()
    type = STABLE
    modLoaders = listOf(loader)

    fun versionList(key: String) = commonProject.providers.gradleProperty("pub.$key")
        .filter { it.isNotBlank() }
        .map { it.split(",") }

    curseforge {
        accessToken = commonProject.providers.gradleProperty("curseforge.token")
        projectId = commonProject.providers.gradleProperty("pub.curseforge.id")
        projectSlug = commonProject.providers.gradleProperty("pub.curseforge.slug")

        minecraftVersions.addAll(versionList("versions"))
        minecraftVersions.addAll(versionList("versions.curseforge"))

        javaVersions.add(java.toolchain.languageVersion.map { JavaVersion.toVersion(it.asInt()) })

        clientRequired = true
        serverRequired = false
    }

    modrinth {
        accessToken = commonProject.providers.gradleProperty("modrinth.token")
        projectId = commonProject.providers.gradleProperty("pub.modrinth.id")

        minecraftVersions.addAll(versionList("versions"))
        minecraftVersions.addAll(versionList("versions.modrinth"))
    }
}
