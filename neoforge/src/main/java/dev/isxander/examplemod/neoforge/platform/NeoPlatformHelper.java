package dev.isxander.examplemod.neoforge.platform;

import dev.isxander.examplemod.platform.services.PlatformHelper;
import net.neoforged.fml.loading.FMLLoader;

public class NeoPlatformHelper implements PlatformHelper {
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
