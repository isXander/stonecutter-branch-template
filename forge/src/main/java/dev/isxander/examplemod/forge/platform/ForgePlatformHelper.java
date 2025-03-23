package dev.isxander.examplemod.forge.platform;

import dev.isxander.examplemod.platform.services.PlatformHelper;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ForgePlatformHelper implements PlatformHelper {
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }
}