plugins {
    `multiloader-loader`
    id("net.neoforged.moddev.legacyforge")
}

legacyForge {
    version = "${mod.mc}-${commonMod.dep("forge")}"

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

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5")
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

publishMods {
    file = tasks.named<Jar>("reobfJar").flatMap { it.archiveFile }
}
