/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class TDClientEvents {

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(ModHandler.class);
        MinecraftForge.EVENT_BUS.register(ForgeHandler.class);
    }

    public static final class ModHandler {

    }

    public static final class ForgeHandler {

    }
}
