package dev.isxander.examplemod;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class ExampleMod {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        LOGGER.info("Hello World!");
    }
}
