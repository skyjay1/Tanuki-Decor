/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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

        public static final RegistryObject<Block> LIBRARY_CLOCK = registerWithMultiblockItem("library_clock", () ->
                new LibraryClockBlock(SoundReg.GRANDFATHER_CLOCK_TICK, SoundReg.GRANDFATHER_CLOCK_CHIME,
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
            return registerWithItem(name, supplier, block -> ItemReg.register(block.getId().getPath(), () -> new MultiblockItem(block.get(), new Item.Properties().stacksTo(1))));
        }
    }

    public static final class ItemReg {

        private static void register() {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        /**
         * Creates a registry object for a block item and adds it to the mod creative tab
         * @param block the block
         * @return the registry object
         */
        private static RegistryObject<Item> registerBlockItem(final RegistryObject<Block> block) {
            return register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
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

        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> LIBRARY_CLOCK_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("library_clock", () ->
                BlockEntityType.Builder.of((pos, state) -> new ClockBlockEntity(BlockEntityReg.LIBRARY_CLOCK_BLOCK_ENTITY.get(), pos, state),
                                BlockReg.LIBRARY_CLOCK.get())
                        .build(null));
    }


    public static final class SoundReg {

        private static void register() {
            SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        private static RegistryObject<SoundEvent> register(final String name) {
            return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(TanukiDecor.MODID, name)));
        }

        public static final RegistryObject<SoundEvent> GRANDFATHER_CLOCK_TICK = register("block.grandfather_clock.tick");
        public static final RegistryObject<SoundEvent> GRANDFATHER_CLOCK_CHIME = register("block.grandfather_clock.chime");
    }
}
