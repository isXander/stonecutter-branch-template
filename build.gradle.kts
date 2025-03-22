plugins {
    `multiloader-common`
    id("net.neoforged.moddev")
}

neoForge {
    neoFormVersion = "${mod.mc}-${mod.dep("neoform")}"

    val at = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }

    parchment {
        mod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = mod.depOrNull("parchment.mc") ?: mod.mc
        }
    }
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")

    "io.github.llamalad7:mixinextras-common:0.3.5".let {
        compileOnly(it)
        annotationProcessor(it)
    }
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}
val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}
artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonResources", sourceSets.main.get().resources.sourceDirectories.singleFile)
}
