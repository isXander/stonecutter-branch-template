package dev.isxander.examplemod.fabric;

import dev.isxander.examplemod.ExampleMod;
import net.fabricmc.api.ModInitializer;

public class Entrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.init();
    }
}
