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
import tanukidecor.TDRegistry.BlockReg;
import tanukidecor.TDRegistry.BlockEntityReg;
import tanukidecor.client.blockentity.clock.*;

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
            // CLOCKS //
            event.registerBlockEntityRenderer(BlockEntityReg.ALARM_CLOCK.get(), AlarmClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.ANNIVERSARY_CLOCK.get(), AnniversaryClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.ANTIQUE_CLOCK.get(), AntiqueClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.BANJO_CLOCK.get(), BanjoClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.BLUE_CLOCK.get(), BlueClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.CARRIAGE_CLOCK.get(), CarriageClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.CRYSTAL_CLOCK.get(), CrystalClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.CUCKOO_CLOCK.get(), CuckooClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.DISPLAY_WATCH.get(), DisplayWatchBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.EMBLEM_CLOCK.get(), EmblemClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.FOLIOT_CLOCK.get(), FoliotClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.GINGERBREAD_CLOCK.get(), GingerbreadClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.GRANDFATHER_CLOCK.get(), GrandfatherClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.GORGEOUS_CLOCK.get(), GorgeousClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.GREEN_CLOCK.get(), GreenClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.LANTERN_CLOCK.get(), LanternClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.LARGE_CLOCK_TOWER_DIAL.get(), LargeClockTowerDialBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.LIBRARY_CLOCK.get(), LibraryClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.MANTLE_CLOCK.get(), MantleClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.MINIMALIST_CLOCK.get(), MinimalistClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.OWL_CLOCK.get(), OwlClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.RECOGNIZABLE_CLOCK.get(), RecognizableClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.RED_CLOCK.get(), RedClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.REED_CLOCK.get(), ReedClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.REGAL_CLOCK.get(), RegalClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.ROCOCO_CLOCK.get(), RococoClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.SMALL_CLOCK_TOWER_DIAL.get(), SmallClockTowerDialBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.SLATE_CLOCK.get(), SlateClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.STATION_CLOCK.get(), StationClockBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.WOODEN_BLOCK_CLOCK.get(), WoodenBlockClockBER::new);
        }

        @SubscribeEvent
        public static void onRegisterModels(final ModelRegistryEvent event) {
            // gather special models
            final Set<ResourceLocation> set = new HashSet<>();
            AlarmClockBER.addSpecialModels(set);
            AnniversaryClockBER.addSpecialModels(set);
            AntiqueClockBER.addSpecialModels(set);
            BanjoClockBER.addSpecialModels(set);
            BlueClockBER.addSpecialModels(set);
            CarriageClockBER.addSpecialModels(set);
            CrystalClockBER.addSpecialModels(set);
            CuckooClockBER.addSpecialModels(set);
            DisplayWatchBER.addSpecialModels(set);
            EmblemClockBER.addSpecialModels(set);
            FoliotClockBER.addSpecialModels(set);
            GingerbreadClockBER.addSpecialModels(set);
            GorgeousClockBER.addSpecialModels(set);
            GrandfatherClockBER.addSpecialModels(set);
            GreenClockBER.addSpecialModels(set);
            LanternClockBER.addSpecialModels(set);
            LargeClockTowerDialBER.addSpecialModels(set);
            LibraryClockBER.addSpecialModels(set);
            MantleClockBER.addSpecialModels(set);
            MinimalistClockBER.addSpecialModels(set);
            OwlClockBER.addSpecialModels(set);
            RecognizableClockBER.addSpecialModels(set);
            RedClockBER.addSpecialModels(set);
            ReedClockBER.addSpecialModels(set);
            RegalClockBER.addSpecialModels(set);
            RococoClockBER.addSpecialModels(set);
            SmallClockTowerDialBER.addSpecialModels(set);
            SlateClockBER.addSpecialModels(set);
            StationClockBER.addSpecialModels(set);
            WoodenBlockClockBER.addSpecialModels(set);
            // register special models
            set.forEach(ForgeModelBakery::addSpecialModel);
        }

        /**
         * Register blocks that use something other than the solid render layer
         */
        private static void registerBlockRenderLayers() {
            // CLOCKS //
            registerRenderLayer(BlockReg.ALARM_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.ANNIVERSARY_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.ANTIQUE_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.BANJO_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.BLUE_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.CARRIAGE_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.CRYSTAL_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.DISPLAY_WATCH.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.FOLIOT_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GINGERBREAD_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GORGEOUS_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GRANDFATHER_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.LANTERN_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.LIBRARY_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.RECOGNIZABLE_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.RED_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.ROCOCO_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.SLATE_CLOCK.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.WOODEN_BLOCK_CLOCK.get(), RenderType.cutout());
            // STORAGE //
            registerRenderLayer(BlockReg.ANTIQUE_CABINET.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.ANTIQUE_WALL_SHELF.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GREEN_PANTRY.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_ARMOIRE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_BOOKSHELF.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_DRESSER.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.SWEETS_CLOSET.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.SWEETS_DRESSER.get(), RenderType.cutout());
        }

        private static void registerRenderLayer(final Block block, RenderType renderType) {
            ItemBlockRenderTypes.setRenderLayer(block, renderType);
        }

    }
}
