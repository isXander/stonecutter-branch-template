plugins {
    `multiloader-loader`
    id("fabric-loom")
}


// Add your dependencies here
dependencies {
    modImplementation("net.fabricmc.fabric-api:fabric-api:${commonMod.dep("fabric-api")}")
}

// Configure mixins here, other parts of this buildscript will be configured for you.
val mixinConfigs = listOf(
    "${mod.id}.mixins.json",
    "${mod.id}.fabric.mixins.json",
)

// Configure mod publishing here.
// The common configuration is in plugin `multiloader-loader`
publishMods {
    file = tasks.remapJar.flatMap { it.archiveFile }

    curseforge {
        requires("fabric-api")
    }
    modrinth {
        requires("fabric-api")
    }
}



// Required dependencies that are automatically configured
dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${commonMod.depOrNull("parchment.mc") ?: commonMod.mc}:$parchmentVersion@zip")
        }
    })
    modImplementation("net.fabricmc:fabric-loader:${commonMod.dep("fabric-loader")}")
}

loom {
    // Adds AW automatically if it's in the common resources.
    val aw = common.project.file("src/main/resources/${mod.id}.accesswidener")
    if (aw.exists()) {
        accessWidenerPath = aw
    }

    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
        }
    }

    mixin {
        // Modify refmap name to be consistent with other platforms.
        defaultRefmapName = "${mod.id}.refmap.json"
    }
}

extraProcessResourceKeys["mixins"] = mixinConfigs.joinToString(separator = "\",\"")
