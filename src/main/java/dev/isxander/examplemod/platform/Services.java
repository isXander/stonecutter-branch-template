package dev.isxander.examplemod.platform;

import dev.isxander.examplemod.ExampleMod;
import dev.isxander.examplemod.platform.services.PlatformHelper;

import java.util.ServiceLoader;

public class Services {
    private Services() {}

    public static final PlatformHelper PLATFORM = load(PlatformHelper.class);

    public static <T> T load(Class<T> service) {
        T loadedService = ServiceLoader.load(service)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No implementation found for " + service.getName()));
        ExampleMod.LOGGER.info("Loaded {} for service {}", loadedService, service);
        return loadedService;
    }
}
