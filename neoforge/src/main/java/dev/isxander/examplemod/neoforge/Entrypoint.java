package dev.isxander.examplemod.neoforge;

import dev.isxander.examplemod.ExampleMod;
import net.neoforged.fml.common.Mod;

@Mod("examplemod")
public class Entrypoint {
    public Entrypoint() {
        ExampleMod.init();
    }
}
