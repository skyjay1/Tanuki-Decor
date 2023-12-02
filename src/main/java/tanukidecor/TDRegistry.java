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
import tanukidecor.block.EmblemClockBlock;
import tanukidecor.block.FoliotClockBlock;
import tanukidecor.block.GrandfatherClockBlock;
import tanukidecor.block.LibraryClockBlock;
import tanukidecor.block.entity.ClockBlockEntity;
import tanukidecor.item.MultiblockItem;

import java.util.function.Function;
import java.util.function.Supplier;

public final class TDRegistry {

    private static final String MODID = TanukiDecor.MODID;

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

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

        public static final RegistryObject<Block> EMBLEM_CLOCK = registerWithMultiblockItem("emblem_clock", () ->
                new EmblemClockBlock(SoundReg.CLOCK_TOWER_TICK, SoundReg.CLOCK_TOWER_CHIME,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 80.0F))
        );
        public static final RegistryObject<Block> FOLIOT_CLOCK = registerWithItem("foliot_clock", () ->
                new FoliotClockBlock(SoundReg.FOLIOT_CLOCK_TICK,
                        BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.0F, 10.0F))
        );
        public static final RegistryObject<Block> LIBRARY_CLOCK = registerWithMultiblockItem("library_clock", () ->
                new LibraryClockBlock(SoundReg.GRANDFATHER_CLOCK_TICK, SoundReg.GRANDFATHER_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 60.0F))
        );
        public static final RegistryObject<Block> GRANDFATHER_CLOCK = registerWithMultiblockItem("grandfather_clock", () ->
                new GrandfatherClockBlock(SoundReg.GRANDFATHER_CLOCK_TICK, SoundReg.GRANDFATHER_CLOCK_CHIME,
                        BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 60.0F))
        );

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

        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> EMBLEM_CLOCK = BLOCK_ENTITY_TYPES.register("emblem_clock", () ->
                BlockEntityType.Builder.of((pos, state) -> new ClockBlockEntity(BlockEntityReg.EMBLEM_CLOCK.get(), pos, state),
                                BlockReg.EMBLEM_CLOCK.get())
                        .build(null));
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> FOLIOT_CLOCK = BLOCK_ENTITY_TYPES.register("foliot_clock", () ->
                BlockEntityType.Builder.of((pos, state) -> new ClockBlockEntity(BlockEntityReg.FOLIOT_CLOCK.get(), pos, state),
                                BlockReg.FOLIOT_CLOCK.get())
                        .build(null));
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> LIBRARY_CLOCK = BLOCK_ENTITY_TYPES.register("library_clock", () ->
                BlockEntityType.Builder.of((pos, state) -> new ClockBlockEntity(BlockEntityReg.LIBRARY_CLOCK.get(), pos, state),
                                BlockReg.LIBRARY_CLOCK.get())
                        .build(null));
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> GRANDFATHER_CLOCK = BLOCK_ENTITY_TYPES.register("grandfather_clock", () ->
                BlockEntityType.Builder.of((pos, state) -> new ClockBlockEntity(BlockEntityReg.GRANDFATHER_CLOCK.get(), pos, state),
                                BlockReg.GRANDFATHER_CLOCK.get())
                        .build(null));
    }


    public static final class SoundReg {

        private static void register() {
            SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        private static RegistryObject<SoundEvent> register(final String name) {
            return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(TanukiDecor.MODID, name)));
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

    }
}
