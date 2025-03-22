package dev.isxander.examplemod.fabric.platform;

import dev.isxander.examplemod.platform.services.PlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements PlatformHelper {
    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
