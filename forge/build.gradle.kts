plugins {
    `multiloader-loader`
    id("net.neoforged.moddev.legacyforge")
}

// Enables MDG so we can use their tasks
legacyForge {
    enable {
        forgeVersion = "${mod.mc}-${commonMod.dep("forge")}"
    }
}



// Add your dependencies here
dependencies {

}

// Configure mixins here, other parts of this buildscript will be configured for you.
val mixinConfigs = listOf(
    "${mod.id}.mixins.json",
    "${mod.id}.forge.mixins.json",
)

// Configure mod publishing here.
// The common configuration is in plugin `multiloader-loader`
publishMods {
    file = tasks.named<Jar>("reobfJar").flatMap { it.archiveFile }
}



// Configure ModDevGradle here
legacyForge {
    // Automatically applies AT if it's in the common resources
    val at = common.project.file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }

    runs {
        register("client") {
            client()
            ideName = "Forge Client (${project.path})"
        }
        register("server") {
            server()
            ideName = "Forge Server (${project.path})"
        }
    }

    parchment {
        commonMod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = commonMod.depOrNull("parchment.mc") ?: commonMod.mc
        }
    }

    mods {
        register(commonMod.id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

// Ensures data-gen gets included in the jar
sourceSets.main {
    resources.srcDir("src/generated/resources")
}

// Configuration for Mixin
mixin {
    add(sourceSets.main.get(), "${mod.id}.refmap.json")
    mixinConfigs.forEach(::config)
}
tasks.jar {
    manifest.attributes["MixinConfigs"] = mixinConfigs.joinToString(",")
}
dependencies {
    // It's important that we add the Mixin AP for refmap generation.
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}
