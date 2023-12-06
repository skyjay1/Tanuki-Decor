/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tanukidecor.block.clock.*;
import tanukidecor.block.entity.*;
import tanukidecor.block.storage.*;
import tanukidecor.item.MultiblockItem;

import java.util.function.Function;
import java.util.function.Supplier;

public final class TDRegistry {

    private static final String MODID = TanukiDecor.MODID;

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

    // NOTE first priority is clocks, then storage, seating, bedding, lighting, and misc

    public static void register() {
        BlockReg.register();
        ItemReg.register();
        BlockEntityReg.register();
        SoundReg.register();
    }

    public static final class BlockReg {

        private static void register() {
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        // CLOCKS //

        public static final RegistryObject<Block> ALARM_CLOCK = registerWithItem("alarm_clock", () ->
                new AlarmClockBlock(SoundReg.ALARM_CLOCK_TICK, SoundReg.ALARM_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> ANNIVERSARY_CLOCK = registerWithItem("anniversary_clock", () ->
                new AnniversaryClockBlock(SoundReg.POCKET_WATCH_TICK,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> ANTIQUE_CLOCK = registerWithItem("antique_clock", () ->
                new AntiqueClock(SoundReg.GRANDFATHER_CLOCK_TICK, SoundReg.GRANDFATHER_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.0F, 10.0F))
        );
        public static final RegistryObject<Block> BANJO_CLOCK = registerWithItem("banjo_clock", () ->
                new BanjoClockBlock(SoundReg.MEDIUM_CLOCK_TICK, SoundReg.MEDIUM_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 10.0F))
        );
        public static final RegistryObject<Block> BLUE_CLOCK = registerWithItem("blue_clock", () ->
                new BlueClock(SoundReg.GRANDFATHER_CLOCK_TICK, SoundReg.GRANDFATHER_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 10.0F))
        );
        public static final RegistryObject<Block> CARRIAGE_CLOCK = registerWithItem("carriage_clock", () ->
                new CarriageClockBlock(SoundReg.POCKET_WATCH_TICK, SoundReg.SLATE_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 8.0F))
        );
        public static final RegistryObject<Block> CRYSTAL_CLOCK = registerWithItem("crystal_clock", () ->
                new CrystalClockBlock(SoundReg.MANTLE_CLOCK_TICK, SoundReg.LANTERN_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> DISPLAY_WATCH = registerWithItem("display_watch", () ->
                new DisplayWatchBlock(SoundReg.POCKET_WATCH_TICK,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> CUCKOO_CLOCK = registerWithItem("cuckoo_clock", () ->
                new CuckooClock(SoundReg.CUCKOO_CLOCK_TICK, SoundReg.CUCKOO_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> EMBLEM_CLOCK = registerWithMultiblockItem("emblem_clock", () ->
                new EmblemClockBlock(SoundReg.CLOCK_TOWER_TICK, SoundReg.CLOCK_TOWER_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 80.0F))
        );
        public static final RegistryObject<Block> FOLIOT_CLOCK = registerWithItem("foliot_clock", () ->
                new FoliotClockBlock(SoundReg.FOLIOT_CLOCK_TICK,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 10.0F))
        );
        // TODO gingerbread clock
        public static final RegistryObject<Block> GORGEOUS_CLOCK = registerWithItem("gorgeous_clock", () ->
                new GorgeousClockBlock(SoundReg.MEDIUM_CLOCK_TICK, SoundReg.MEDIUM_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.0F, 10.0F))
        );
        public static final RegistryObject<Block> GRANDFATHER_CLOCK = registerWithMultiblockItem("grandfather_clock", () ->
                new GrandfatherClockBlock(SoundReg.GRANDFATHER_CLOCK_TICK, SoundReg.GRANDFATHER_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 60.0F))
        );
        public static final RegistryObject<Block> GREEN_CLOCK = registerWithItem("green_clock", () ->
                new GreenClock(SoundReg.ALARM_CLOCK_TICK,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> LANTERN_CLOCK = registerWithItem("lantern_clock", () ->
                new LanternClockBlock(SoundReg.FOLIOT_CLOCK_TICK, SoundReg.LANTERN_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> LARGE_CLOCK_TOWER_DIAL = registerWithMultiblockItem("large_clock_tower_dial", () ->
                new LargeClockTowerDialBlock(SoundReg.CLOCK_TOWER_TICK, SoundReg.CLOCK_TOWER_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 80.0F))
        );
        public static final RegistryObject<Block> LIBRARY_CLOCK = registerWithMultiblockItem("library_clock", () ->
                new LibraryClockBlock(SoundReg.GRANDFATHER_CLOCK_TICK, SoundReg.GRANDFATHER_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 60.0F))
        );
        public static final RegistryObject<Block> MANTLE_CLOCK = registerWithItem("mantle_clock", () ->
                new MantleClockBlock(SoundReg.MANTLE_CLOCK_TICK, SoundReg.MANTLE_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> MINIMALIST_CLOCK = registerWithItem("minimalist_clock", () ->
                new MinimalistClock(SoundReg.ALARM_CLOCK_TICK,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> OWL_CLOCK = registerWithItem("owl_clock", () ->
                new OwlClock(SoundReg.MEDIUM_CLOCK_TICK2,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> RECOGNIZABLE_CLOCK = registerWithItem("recognizable_clock", () ->
                new RecognizableClockBlock(SoundReg.GRANDFATHER_CLOCK_TICK, SoundReg.RECOGNIZABLE_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.0F, 10.0F))
        );
        public static final RegistryObject<Block> RED_CLOCK = registerWithItem("red_clock", () ->
                new RedClockBlock(SoundReg.ALARM_CLOCK_TICK, SoundReg.ALARM_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> REED_CLOCK = registerWithItem("reed_clock", () ->
                new ReedClockBlock(SoundReg.MEDIUM_CLOCK_TICK,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> REGAL_CLOCK = registerWithItem("regal_clock", () ->
                new RegalClockBlock(SoundReg.GRANDFATHER_CLOCK_TICK,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 12.0F))
        );
        public static final RegistryObject<Block> ROCOCO_CLOCK = registerWithItem("rococo_clock", () ->
                new RococoClockBlock(SoundReg.MANTLE_CLOCK_TICK, SoundReg.LANTERN_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 6.0F))
        );
        public static final RegistryObject<Block> SLATE_CLOCK = registerWithItem("slate_clock", () ->
                new SlateClockBlock(SoundReg.MANTLE_CLOCK_TICK, SoundReg.SLATE_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 6.0F))
        );
        public static final RegistryObject<Block> SMALL_CLOCK_TOWER_DIAL = registerWithMultiblockItem("small_clock_tower_dial", () ->
                new SmallClockTowerDialBlock(SoundReg.CLOCK_TOWER_TICK, SoundReg.CLOCK_TOWER_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 60.0F))
        );
        public static final RegistryObject<Block> STATION_CLOCK = registerWithMultiblockItem("station_clock", () ->
                new StationClockBlock(SoundReg.CLOCK_TOWER_TICK,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 60.0F))
        );
        public static final RegistryObject<Block> WOODEN_BLOCK_CLOCK = registerWithItem("wooden_block_clock", () ->
                new WoodenBlockClockBlock(SoundReg.CUCKOO_CLOCK_TICK,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 4.0F))
        );

        // STORAGE //
        public static final RegistryObject<Block> ANTIQUE_BUREAU = registerWithItem("antique_bureau", () ->
                new AntiqueBureauBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F))
        );
        public static final RegistryObject<Block> ANTIQUE_CABINET = registerWithMultiblockItem("antique_cabinet", () ->
                new AntiqueCabinetBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F))
        );
        public static final RegistryObject<Block> ANTIQUE_DESK = registerWithMultiblockItem("antique_desk", () ->
                new AntiqueDeskBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F))
        );
        public static final RegistryObject<Block> ANTIQUE_MIRROR = registerWithMultiblockItem("antique_mirror", () ->
                new AntiqueMirrorBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F))
        );
        public static final RegistryObject<Block> ANTIQUE_WALL_SHELF = registerWithItem("antique_wall_shelf", () ->
                new AntiqueWallShelfBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> ANTIQUE_WARDROBE = registerWithMultiblockItem("antique_wardrobe", () ->
                new AntiqueWardrobeBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F))
        );
        public static final RegistryObject<Block> GREEN_DESK = registerWithItem("green_desk", () ->
                new GreenDeskBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F))
        );
        public static final RegistryObject<Block> GREEN_DRESSER = registerWithMultiblockItem("green_dresser", () ->
                new GreenDresserBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F))
        );

        // TODO

        private static RegistryObject<Block> registerWithItem(final String name, final Supplier<Block> supplier) {
            return registerWithItem(name, supplier, ItemReg::registerBlockItem);
        }

        private static RegistryObject<Block> registerWithItem(final String name, final Supplier<Block> blockSupplier, final Function<RegistryObject<Block>, RegistryObject<Item>> itemSupplier) {
            final RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
            final RegistryObject<Item> item = itemSupplier.apply(block);
            return block;
        }

        private static RegistryObject<Block> registerWithMultiblockItem(final String name, final Supplier<Block> supplier) {
            return registerWithItem(name, supplier, block -> ItemReg.register(block.getId().getPath(), () -> new MultiblockItem(block.get(), new Item.Properties().tab(ItemReg.TAB).stacksTo(1))));
        }
    }

    public static final class ItemReg {

        private static void register() {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final CreativeModeTab TAB = new CreativeModeTab(TanukiDecor.MODID) {
            @Override
            public ItemStack makeIcon() {
                return RegistryObject.create(new ResourceLocation(TanukiDecor.MODID, "emblem_clock"), ForgeRegistries.ITEMS).get().getDefaultInstance();
            }
        };

        /**
         * Creates a registry object for a block item and adds it to the mod creative tab
         * @param block the block
         * @return the registry object
         */
        private static RegistryObject<Item> registerBlockItem(final RegistryObject<Block> block) {
            return register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(TAB)));
        }

        /**
         * Creates a registry object for the given item and adds it to the mod creative tab
         * @param name the registry name
         * @param supplier the item supplier
         * @return the item registry object
         */
        private static RegistryObject<Item> register(final String name, final Supplier<Item> supplier) {
            return ITEMS.register(name, supplier);
        }
    }

    public static final class BlockEntityReg {

        private static void register() {
            BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        // CLOCKS //

        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> ALARM_CLOCK = registerClock(
                () -> BlockEntityReg.ALARM_CLOCK, BlockReg.ALARM_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> ANNIVERSARY_CLOCK = registerClock(
                () -> BlockEntityReg.ANNIVERSARY_CLOCK, BlockReg.ANNIVERSARY_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> ANTIQUE_CLOCK = registerClock(
                () -> BlockEntityReg.ANTIQUE_CLOCK, BlockReg.ANTIQUE_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> BANJO_CLOCK = registerClock(
                () -> BlockEntityReg.BANJO_CLOCK, BlockReg.BANJO_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> BLUE_CLOCK = registerClock(
                () -> BlockEntityReg.BLUE_CLOCK, BlockReg.BLUE_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> CARRIAGE_CLOCK = registerClock(
                () -> BlockEntityReg.CARRIAGE_CLOCK, BlockReg.CARRIAGE_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> CRYSTAL_CLOCK = registerClock(
                () -> BlockEntityReg.CRYSTAL_CLOCK, BlockReg.CRYSTAL_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> CUCKOO_CLOCK = registerClock(
                () -> BlockEntityReg.CUCKOO_CLOCK, BlockReg.CUCKOO_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> DISPLAY_WATCH = registerClock(
                () -> BlockEntityReg.DISPLAY_WATCH, BlockReg.DISPLAY_WATCH);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> EMBLEM_CLOCK = registerClock(
                () -> BlockEntityReg.EMBLEM_CLOCK, BlockReg.EMBLEM_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> FOLIOT_CLOCK = registerClock(
                () -> BlockEntityReg.FOLIOT_CLOCK, BlockReg.FOLIOT_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> GORGEOUS_CLOCK = registerClock(
                () -> BlockEntityReg.GORGEOUS_CLOCK, BlockReg.GORGEOUS_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> GRANDFATHER_CLOCK = registerClock(
                () -> BlockEntityReg.GRANDFATHER_CLOCK, BlockReg.GRANDFATHER_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> GREEN_CLOCK = registerClock(
                () -> BlockEntityReg.GREEN_CLOCK, BlockReg.GREEN_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> LANTERN_CLOCK = registerClock(
                () -> BlockEntityReg.LANTERN_CLOCK, BlockReg.LANTERN_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> LARGE_CLOCK_TOWER_DIAL = registerClock(
                () -> BlockEntityReg.LARGE_CLOCK_TOWER_DIAL, BlockReg.LARGE_CLOCK_TOWER_DIAL);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> LIBRARY_CLOCK = registerClock(
                () -> BlockEntityReg.LIBRARY_CLOCK, BlockReg.LIBRARY_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> MANTLE_CLOCK = registerClock(
                () -> BlockEntityReg.MANTLE_CLOCK, BlockReg.MANTLE_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> MINIMALIST_CLOCK = registerClock(
                () -> BlockEntityReg.MINIMALIST_CLOCK, BlockReg.MINIMALIST_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> OWL_CLOCK = registerClock(
                () -> BlockEntityReg.OWL_CLOCK, BlockReg.OWL_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> RECOGNIZABLE_CLOCK = registerClock(
                () -> BlockEntityReg.RECOGNIZABLE_CLOCK, BlockReg.RECOGNIZABLE_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> RED_CLOCK = registerClock(
                () -> BlockEntityReg.RED_CLOCK, BlockReg.RED_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> REED_CLOCK = registerClock(
                () -> BlockEntityReg.REED_CLOCK, BlockReg.REED_CLOCK);
            public static final RegistryObject<BlockEntityType<ClockBlockEntity>> REGAL_CLOCK = registerClock(
                () -> BlockEntityReg.REGAL_CLOCK, BlockReg.REGAL_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> ROCOCO_CLOCK = registerClock(
                () -> BlockEntityReg.ROCOCO_CLOCK, BlockReg.ROCOCO_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> SLATE_CLOCK = registerClock(
                () -> BlockEntityReg.SLATE_CLOCK, BlockReg.SLATE_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> SMALL_CLOCK_TOWER_DIAL = registerClock(
                () -> BlockEntityReg.SMALL_CLOCK_TOWER_DIAL, BlockReg.SMALL_CLOCK_TOWER_DIAL);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> STATION_CLOCK = registerClock(
                () -> BlockEntityReg.STATION_CLOCK, BlockReg.STATION_CLOCK);
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> WOODEN_BLOCK_CLOCK = registerClock(
                () -> BlockEntityReg.WOODEN_BLOCK_CLOCK, BlockReg.WOODEN_BLOCK_CLOCK);

        // STORAGE //
        public static final RegistryObject<BlockEntityType<StorageDelegateBlockEntity>> STORAGE_DELEGATE = BLOCK_ENTITY_TYPES.register("storage_delegate", () -> BlockEntityType.Builder
                .of((pos, state) -> new StorageDelegateBlockEntity(BlockEntityReg.STORAGE_DELEGATE.get(), pos, state),
                        BlockReg.ANTIQUE_BUREAU.get(), BlockReg.ANTIQUE_DESK.get(), BlockReg.ANTIQUE_MIRROR.get(), BlockReg.ANTIQUE_WARDROBE.get())
                .build(null));

        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_BUREAU = registerStorage(
                () -> BlockEntityReg.ANTIQUE_BUREAU, 6, BlockReg.ANTIQUE_BUREAU);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_CABINET = registerStorage(
                () -> BlockEntityReg.ANTIQUE_CABINET, 6, BlockReg.ANTIQUE_CABINET);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_DESK = registerStorage(
                () -> BlockEntityReg.ANTIQUE_DESK, 6, BlockReg.ANTIQUE_DESK);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_MIRROR = registerStorage(
                () -> BlockEntityReg.ANTIQUE_MIRROR, 6, BlockReg.ANTIQUE_MIRROR);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_WARDROBE = registerStorage(
                () -> BlockEntityReg.ANTIQUE_WARDROBE, 6, BlockReg.ANTIQUE_WARDROBE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_WALL_SHELF = registerStorage(
                () -> BlockEntityReg.ANTIQUE_WALL_SHELF, 3, BlockReg.ANTIQUE_WALL_SHELF);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GREEN_DESK = registerStorage(
                () -> BlockEntityReg.GREEN_DESK, 3, BlockReg.GREEN_DESK);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GREEN_DRESSER = registerStorage(
                () -> BlockEntityReg.GREEN_DRESSER, 3, BlockReg.GREEN_DRESSER);

        /**
         * @param type the supplier for the block entity type registry object
         * @param block the block registry object for the block entity type
         * @return the registered block entity type for the ClockBlockEntity
         */
        private static RegistryObject<BlockEntityType<ClockBlockEntity>> registerClock(final Supplier<Supplier<BlockEntityType<ClockBlockEntity>>> type, final RegistryObject<Block> block) {
            return BLOCK_ENTITY_TYPES.register(block.getId().getPath(), () -> BlockEntityType.Builder
                    .of((pos, state) -> new ClockBlockEntity(type.get().get(), pos, state), block.get())
                    .build(null));
        }

        /**
         * @param type the supplier for the block entity type registry object
         * @param rows the number of inventory rows from 1 to 6
         * @param block the block registry object for the block entity type
         * @return the registered block entity type for the StorageBlockEntity
         */
        private static RegistryObject<BlockEntityType<StorageBlockEntity>> registerStorage(final Supplier<Supplier<BlockEntityType<StorageBlockEntity>>> type, final int rows, final RegistryObject<Block> block) {
            if(rows < 1 || rows > 6) {
                throw new IllegalArgumentException("[TDRegistry.BlockEntityReg.registerStorage] rows=" + rows + " is out of bounds for range [1,6]");
            }
            return BLOCK_ENTITY_TYPES.register(block.getId().getPath(), () -> BlockEntityType.Builder
                    .of((pos, state) -> new StorageBlockEntity(type.get().get(), pos, state, rows), block.get())
                    .build(null));
        }
    }


    public static final class SoundReg {

        private static void register() {
            SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final RegistryObject<SoundEvent> ALARM_CLOCK_TICK = register("block.alarm_clock.tick");
        public static final RegistryObject<SoundEvent> ALARM_CLOCK_CHIME = register("block.alarm_clock.chime");
        public static final RegistryObject<SoundEvent> CLOCK_TOWER_TICK = register("block.clock_tower.tick");
        public static final RegistryObject<SoundEvent> CLOCK_TOWER_CHIME = register("block.clock_tower.chime");
        public static final RegistryObject<SoundEvent> CUCKOO_CLOCK_TICK = register("block.cuckoo_clock.tick");
        public static final RegistryObject<SoundEvent> CUCKOO_CLOCK_CHIME = register("block.cuckoo_clock.chime");
        public static final RegistryObject<SoundEvent> FOLIOT_CLOCK_TICK = register("block.foliot_clock.tick");
        public static final RegistryObject<SoundEvent> GRANDFATHER_CLOCK_TICK = register("block.grandfather_clock.tick");
        public static final RegistryObject<SoundEvent> GRANDFATHER_CLOCK_CHIME = register("block.grandfather_clock.chime");
        public static final RegistryObject<SoundEvent> LANTERN_CLOCK_CHIME = register("block.lantern_clock.chime");
        public static final RegistryObject<SoundEvent> MANTLE_CLOCK_TICK = register("block.mantle_clock.tick");
        public static final RegistryObject<SoundEvent> MANTLE_CLOCK_CHIME = register("block.mantle_clock.chime");
        public static final RegistryObject<SoundEvent> MEDIUM_CLOCK_TICK = register("block.medium_clock.tick");
        public static final RegistryObject<SoundEvent> MEDIUM_CLOCK_TICK2 = register("block.medium_clock.tick2");
        public static final RegistryObject<SoundEvent> MEDIUM_CLOCK_CHIME = register("block.medium_clock.chime");
        public static final RegistryObject<SoundEvent> POCKET_WATCH_TICK = register("block.pocket_watch.tick");
        public static final RegistryObject<SoundEvent> RECOGNIZABLE_CLOCK_CHIME = register("block.recognizable_clock.chime");
        public static final RegistryObject<SoundEvent> SLATE_CLOCK_CHIME = register("block.slate_clock.chime");

        /**
         * @param name the sound name as specified in the sounds.json file
         * @return a registered sound event for the TanukiDecor namespace and the given sound name
         */
        private static RegistryObject<SoundEvent> register(final String name) {
            return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(TanukiDecor.MODID, name)));
        }
    }
}
