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
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tanukidecor.TDRegistry;
import tanukidecor.TanukiDecor;
import tanukidecor.client.blockentity.LibraryClockBlockEntityRenderer;

import java.util.LinkedList;
import java.util.List;

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
            event.enqueueWork(ModHandler::registerBlockRenderLayers);
        }

        @SubscribeEvent
        public static void onRegisterEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(TDRegistry.BlockEntityReg.LIBRARY_CLOCK_BLOCK_ENTITY.get(), LibraryClockBlockEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerModel(final ModelRegistryEvent event) {
            // gather special models
            final List<ResourceLocation> list = new LinkedList<>();
            LibraryClockBlockEntityRenderer.addSpecialModels(list);
            // register special models
            list.forEach(ForgeModelBakery::addSpecialModel);
        }

        private static void registerBlockRenderLayers() {
            registerRenderLayer(TDRegistry.BlockReg.LIBRARY_CLOCK.get(), RenderType.cutout());
        }

        private static void registerRenderLayer(final Block block, RenderType renderType) {
            ItemBlockRenderTypes.setRenderLayer(block, renderType);
        }

    }
}
