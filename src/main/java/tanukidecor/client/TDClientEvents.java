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
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tanukidecor.TDRegistry;
import tanukidecor.client.blockentity.*;

import java.util.HashSet;
import java.util.Set;

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
            event.registerBlockEntityRenderer(TDRegistry.BlockEntityReg.EMBLEM_CLOCK.get(), EmblemClockBER::new);
            event.registerBlockEntityRenderer(TDRegistry.BlockEntityReg.FOLIOT_CLOCK.get(), FoliotClockBER::new);
            event.registerBlockEntityRenderer(TDRegistry.BlockEntityReg.LIBRARY_CLOCK.get(), LibraryClockBER::new);
            event.registerBlockEntityRenderer(TDRegistry.BlockEntityReg.GRANDFATHER_CLOCK.get(), GrandfatherClockBER::new);
        }

        @SubscribeEvent
        public static void registerModel(final ModelRegistryEvent event) {
            // gather special models
            final Set<ResourceLocation> set = new HashSet<>();
            LibraryClockBER.addSpecialModels(set);
            GrandfatherClockBER.addSpecialModels(set);
            EmblemClockBER.addSpecialModels(set);
            FoliotClockBER.addSpecialModels(set);
            // register special models
            set.forEach(ForgeModelBakery::addSpecialModel);
        }

        private static void registerBlockRenderLayers() {
            registerRenderLayer(TDRegistry.BlockReg.LIBRARY_CLOCK.get(), RenderType.cutout());
        }

        private static void registerRenderLayer(final Block block, RenderType renderType) {
            ItemBlockRenderTypes.setRenderLayer(block, renderType);
        }

    }
}
