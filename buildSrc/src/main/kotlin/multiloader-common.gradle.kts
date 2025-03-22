plugins {
    `java-library`
    `maven-publish`
}

version = "${commonMod.version}+${loader}-${stonecutterBuild.current.version}"
base {
    archivesName = "${commonMod.id}-${loader}-${commonMod.mc}"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(commonProject.prop("java.version")!!)
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    exclusiveContent {
        forRepositories(
            maven("https://maven.parchmentmc.org") { name = "ParchmentMC" },
            maven("https://maven.neoforged.net/releases") { name = "NeoForge" },
        )
        filter { includeGroup("org.parchmentmc.data") }
    }
}

// Declare capabilities on the outgoing configurations.
// Read more about capabilities here: https://docs.gradle.org/current/userguide/component_capabilities.html#sec:declaring-additional-capabilities-for-a-local-component
listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    configurations[variant].outgoing {
        capability("$group:$loader:$version")
        capability("$group:${base.archivesName.get()}:$version")
        capability("$group:${mod.id}-$loader-${stonecutterBuild.current.version}:$version")
        capability("$group:${mod.id}:$version")
    }
    publishing.publications.containerWithType(MavenPublication::class.java).configureEach {
        suppressPomMetadataWarningsFor(variant)
    }
}

tasks {
    named<Jar>("sourcesJar") {
        from(rootProject.file("LICENSE")) {
            rename { "${it}_${commonMod.id}" }
        }
    }

    jar {
        from(rootProject.file("LICENSE")) {
            rename { "${it}_${commonMod.id}" }
        }

        manifest.attributes(
            "Specification-Title" to commonMod.name,
            "Specification-Vendor" to commonMod.author,
            "Specification-Version" to commonMod.version,
        )
    }

    processResources {
        val expandProps = mapOf(
            "mod_version" to commonMod.version,
            "mod_group" to commonMod.group,
            "mod_author" to commonMod.author,
            "minecraft" to commonMod.mc,
            "mod_name" to commonMod.name,
            "mod_id" to commonMod.id,
            "mod_description" to commonMod.description,
            "mod_license" to commonMod.license,
            "fabric.loader_version" to commonMod.depOrNull("fabric-loader"),
            "fabric.minecraft_constraint" to commonMod.propOrNull("meta.fabric.minecraft-constraint"),
            "neoforge.version" to commonMod.depOrNull("neoforge"),
            "neoforge.loader_constraint" to commonMod.propOrNull("meta.neoforge.loader-constraint"),
            "neoforge.minecraft_constraint" to commonMod.propOrNull("meta.neoforge.minecraft-constraint"),
        )

        val jsonExpandProps = expandProps.mapValues { (_, v) -> v?.replace("\n", "\\\\n") }

        filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
            expand(expandProps)
        }
        filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "*.mixins.json")) {
            expand(jsonExpandProps)
        }

        inputs.properties(expandProps)
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }
}
