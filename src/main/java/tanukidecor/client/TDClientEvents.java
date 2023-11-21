/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tanukidecor.TDRegistry;
import tanukidecor.TanukiDecor;

public final class TDClientEvents {

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(ModHandler.class);
        MinecraftForge.EVENT_BUS.register(ForgeHandler.class);
    }

    public static final class ForgeHandler {
    }

    public static final class ModHandler {
        @SubscribeEvent
        public static void onCommonSetup(final FMLCommonSetupEvent event) {

        }

        private static void registerBlockRenderLayers() {
            registerRenderLayer(TDRegistry.BlockReg.LIBRARY_CLOCK.get(), RenderType.cutout());
        }

        private static void registerRenderLayer(final Block block, RenderType renderType) {
            ItemBlockRenderTypes.setRenderLayer(block, renderType);
        }

    }
}
