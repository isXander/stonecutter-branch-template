plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
}

// Enables ModDevGradle as early as possible so we can use their tasks and configurations.
neoForge {
    enable {
        version = commonMod.dep("neoforge")
    }
}



// Add your dependencies here
dependencies {

}

// Configure mixins here, other parts of this buildscript will be configured for you.
val mixinConfigs = listOf(
    "${mod.id}.mixins.json",
    "${mod.id}.neoforge.mixins.json",
)

// Configure mod publishing here
publishMods {
    file = tasks.jar.flatMap { it.archiveFile }

//    modrinth {
//        requires("some-mod")
//    }
}



// ModDevGradle configuration is done here.
neoForge {
    // Accesss transformer is automatically applied if it is present in common.
    val at = common.project.file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }

    runs {
        register("client") {
            client()
            ideName = "NeoForge Client (${project.path})"
        }
        register("server") {
            server()
            ideName = "NeoForge Server (${project.path})"
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

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

// Adds mixins programmatically into the mod manifest.
extraProcessResourceKeys["mixins"] =
    mixinConfigs.joinToString(separator = "\n") { name ->
        """
            [[mixins]]
            config = "$name"
        """.trimIndent()
    }
