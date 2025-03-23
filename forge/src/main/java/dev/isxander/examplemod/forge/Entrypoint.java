package dev.isxander.examplemod.forge;

import dev.isxander.examplemod.ExampleMod;
import net.minecraftforge.fml.common.Mod;

@Mod("examplemod")
public class Entrypoint {
    public Entrypoint() {
        ExampleMod.init();
    }
}
