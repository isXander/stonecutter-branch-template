# Contributing guide

## Tooling

This project uses a wide variety of tools that may be uncommon to see all working together in a single project.

- [Stonecutter](https://stonecutter.kikugie.dev) - A preprocessor to allow supporting multiple Minecraft versions in a single codebase.
- [ModDevGradle](https://projects.neoforged.net/neoforged/moddevgradle) - NeoForge's official Gradle Plugin
  - Used for common subprojects in Vanilla-mode
  - Used for NeoForge subprojects
  - Used for MinecraftForge subprojects in Legacy-mode
- [Fabric Loom](https://github.com/fabricmc/fabric-loom) - Fabric's official Gradle Plugin
  - Used for Fabric subprojects

The choice to use official tooling rather than the cohesive Architectury Loom was done to ensure that
the project can stay on the bleeding edge of each toolchain, and to reduce reliance on third-parties for
functional builds in the future.

### Building

To build all targets of the project, (may take a while)

```shell
./gradlew :chiseledBuild
```

You may also build a specific target by first activating it, then running its build task.

```shell
./gradlew :"Set active project to 1.20.1" :fabric:1.20.1:build
```

### Stonecutter

Stonecutter is a preprocessor that allows for a single codebase to support multiple Minecraft versions.
This plugin dictates quite heavily the structure of the project, buildscripts are re-used by multiple subprojects.

It is suggested you read up on the [Stonecutter documentation](https://stonecutter.kikugie.dev) before contributing.

### Project structure

- `/src` - The common project source directory, code and resources here are shared between all targets.
- `/build.gradle.kts` - The common-project buildscript for versions 1.20.4 and above
- `/legacyforge.gradle.kts` - The common-project buildscript for versions below 1.20.4
  - Code between this and `build.gradle.kts` should be duplicated as closely as possible. This split is necessary
    to support versions below 1.20.4, as a different plugin is required (MDG Legacy).
- `/stonecutter.gradle.kts` - The root project buildscript, nothing modding related happens here, and is used to pre-load
  plugins, setup chiseled tasks, and for defining common tasks shared between all versions of Minecraft.
- `/settings.gradle.kts` - The project settings script, responsible for telling Stonecutter what versions to support.
- `/gradle.properties` - The root properties file, define project-wide properties here.
- `/versions/*/gradle.properties` - The common project's versioned properties file, define version-specific properties here,
  even Fabric and NeoForge and Forge properties, it's encouraged to use the common properties for all loaders to keep it
  all in one place.
  - You can reference properties in here by using `commonMod.prop("key")` from any buildscript.
- `/versions/current` - A file used by Stonecutter to determine the active version, you should not change this and you
  should make sure it does not change when you commit.

Fabric, NeoForge, and Forge targets are split into Stonecutter branches. Each have their own versioned buildscript,
just like the common project. Add target-specific code to the appropriate buildscript.

#### `buildSrc`

Pre-compiled script plugins are defined in the `src` directory of the `buildSrc` project. 

- Modify `multiloader-common.gradle.kts` for common changes across all targets AND common.
- Modify `multiloader-loader.gradle.kts` for common changes across all targets, NOT common.
  