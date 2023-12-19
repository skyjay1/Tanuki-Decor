/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tanukidecor.TDRegistry;
import tanukidecor.TDRegistry.BlockReg;
import tanukidecor.TDRegistry.BlockEntityReg;
import tanukidecor.block.seat.ISeatProvider;
import tanukidecor.client.blockentity.clock.*;
import tanukidecor.client.blockentity.misc.HourglassBER;
import tanukidecor.client.blockentity.misc.PhonographBER;
import tanukidecor.client.blockentity.misc.TrainSetBER;
import tanukidecor.client.menu.DIYWorkbenchScreen;

import java.util.HashSet;
import java.util.Set;

public final class TDClientEvents {

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(ModHandler.class);
        MinecraftForge.EVENT_BUS.register(ForgeHandler.class);
    }

    public static final class ForgeHandler {

        @SubscribeEvent
        public static void onRenderOverlay(final RenderGameOverlayEvent.PreLayer event) {
            final Player player = Minecraft.getInstance().player;
            if(event.getOverlay() == ForgeIngameGui.MOUNT_HEALTH_ELEMENT
                    && player != null && player.isPassenger()
                    && ISeatProvider.IS_SEAT_ENTITY.test(player.getVehicle())) {
                event.setCanceled(true);
            }

        }
    }

    public static final class ModHandler {

        @SubscribeEvent
        public static void onCommonSetup(final FMLCommonSetupEvent event) {
            event.enqueueWork(ModHandler::registerBlockRenderLayers);
            event.enqueueWork(ModHandler::registerMenuScreens);
        }

        private static void registerMenuScreens() {
            MenuScreens.register(TDRegistry.MenuReg.DIY_WORKBENCH.get(), DIYWorkbenchScreen::new);
        }

        /**
         * Register blocks that use something other than the solid render layer
         */
        private static void registerBlockRenderLayers() {
            // CLOCK //
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
            registerRenderLayer(BlockReg.ANTIQUE_BOOKCASE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.ANTIQUE_CABINET.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.ANTIQUE_WALL_SHELF.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GREEN_PANTRY.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_ARMOIRE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_BOOKSHELF.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_DRESSER.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_VANITY.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.SWEETS_CLOSET.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.SWEETS_DRESSER.get(), RenderType.cutout());
            // SEAT //
            registerRenderLayer(BlockReg.BLUE_BENCH.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.BLUE_CHAIR.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GORGEOUS_STOOL.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_CHAIR.get(), RenderType.cutout());
            // BED //
            registerRenderLayer(BlockReg.BLUE_BED.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.EGYPTIAN_BED.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GORGEOUS_BED.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_BED.get(), RenderType.cutout());
            // LIGHT //
            registerRenderLayer(BlockReg.ANTIQUE_WALL_OIL_LAMP.get(), RenderType.translucent());
            registerRenderLayer(BlockReg.BLUE_LAMP.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.CABANA_LAMP.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.EGYPTIAN_LAMP.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GORGEOUS_LAMP.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GREEN_LAMP.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.LARGE_FIREPLACE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.MINIMALIST_LAMP.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.NEON_CLUB_SIGN.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.NEON_DIAMOND_SIGN.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.NEON_HEART_SIGN.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.NEON_SPADE_SIGN.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.REGAL_LAMP.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.SMALL_FIREPLACE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.SWEETS_MINI_LAMP.get(), RenderType.cutout());
            // MISC //
            registerRenderLayer(BlockReg.ANTIQUE_PHONE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.BLUE_TABLE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.DIY_WORKBENCH.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.GUMBALL_MACHINE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.HOLIDAY_TREE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.HOURGLASS.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.PHONOGRAPH.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.SHIP_IN_A_BOTTLE.get(), RenderType.cutout());
            registerRenderLayer(BlockReg.TRAIN_SET.get(), RenderType.cutout());
        }

        private static void registerRenderLayer(final Block block, RenderType renderType) {
            ItemBlockRenderTypes.setRenderLayer(block, renderType);
        }

        @SubscribeEvent
        public static void onRegisterEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            // CLOCK //
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
            // MISC //
            event.registerBlockEntityRenderer(BlockEntityReg.HOURGLASS.get(), HourglassBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.PHONOGRAPH.get(), PhonographBER::new);
            event.registerBlockEntityRenderer(BlockEntityReg.TRAIN_SET.get(), TrainSetBER::new);
        }

        @SubscribeEvent
        public static void onRegisterModels(final ModelRegistryEvent event) {
            // gather special models
            final Set<ResourceLocation> set = new HashSet<>();
            // CLOCK //
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
            // MISC //
            HourglassBER.addSpecialModels(set);
            PhonographBER.addSpecialModels(set);
            TrainSetBER.addSpecialModels(set);
            // register special models
            set.forEach(ForgeModelBakery::addSpecialModel);
        }
    }
}
