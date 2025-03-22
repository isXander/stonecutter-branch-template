plugins {
    `multiloader-loader`
    id("fabric-loom")
}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${commonMod.depOrNull("parchment.mc") ?: commonMod.mc}:$parchmentVersion@zip")
        }
    })
    modImplementation("net.fabricmc:fabric-loader:${commonMod.dep("fabric-loader")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${commonMod.dep("fabric-api")}")
}

loom {
    val aw = common.project.file("src/main/resources/${mod.id}.accesswidener")
    if (aw.exists()) {
        accessWidenerPath = aw
    }
}

publishMods {
    file = tasks.remapJar.flatMap { it.archiveFile }

    curseforge {
        requires("fabric-api")
    }
    modrinth {
        requires("fabric-api")
    }
}