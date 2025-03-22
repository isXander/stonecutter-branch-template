plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
}

neoForge {
    version = commonMod.dep("neoforge")

    val at = common.project.file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
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

publishMods {
    file = tasks.jar.flatMap { it.archiveFile }
}