/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tanukidecor.block.TallBlock;
import tanukidecor.block.RotatingBlock;
import tanukidecor.block.bed.*;
import tanukidecor.block.clock.*;
import tanukidecor.block.entity.*;
import tanukidecor.block.light.*;
import tanukidecor.block.misc.*;
import tanukidecor.block.recipe.DIYRecipe;
import tanukidecor.block.seat.*;
import tanukidecor.block.storage.*;
import tanukidecor.item.MultiblockItem;
import tanukidecor.menu.DIYWorkbenchMenu;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class TDRegistry {

    private static final String MODID = TanukiDecor.MODID;

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, MODID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    // NOTE first priority is clocks, then storage, seating, bedding, lighting, and misc
    // NOTE the following blocks are re-categorized from the specs:
    // ADDED TO STORAGE
    // Misc. 11 Antique bookcase
    // Misc. 22 Regal vanity
    // Misc. 36 Cabana bookcase
    // Misc. 39 Cabana vanity

    public static void register() {
        BlockReg.register();
        ItemReg.register();
        BlockEntityReg.register();
        SoundReg.register();
        RecipeReg.register();
        MenuReg.register();
    }

    public static final class BlockReg {

        private static void register() {
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        // CLOCKS //

        public static final RegistryObject<Block> ALARM_CLOCK = registerWithItem("alarm_clock", () ->
                new AlarmClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> ANNIVERSARY_CLOCK = registerWithItem("anniversary_clock", () ->
                new AnniversaryClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> ANTIQUE_CLOCK = registerWithItem("antique_clock", () ->
                new AntiqueClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> BANJO_CLOCK = registerWithItem("banjo_clock", () ->
                new BanjoClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> BLUE_CLOCK = registerWithItem("blue_clock", () ->
                new BlueClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> CARRIAGE_CLOCK = registerWithItem("carriage_clock", () ->
                new CarriageClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 8.0F)) );
        public static final RegistryObject<Block> CRYSTAL_CLOCK = registerWithItem("crystal_clock", () ->
                new CrystalClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> DISPLAY_WATCH = registerWithItem("display_watch", () ->
                new DisplayWatchBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> CUCKOO_CLOCK = registerWithItem("cuckoo_clock", () ->
                new CuckooClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> EMBLEM_CLOCK = registerWithMultiblockItem("emblem_clock", () ->
                new EmblemClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 80.0F)) );
        public static final RegistryObject<Block> FOLIOT_CLOCK = registerWithItem("foliot_clock", () ->
                new FoliotClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> GINGERBREAD_CLOCK = registerWithItem("gingerbread_clock", () ->
                new GingerbreadClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GORGEOUS_CLOCK = registerWithItem("gorgeous_clock", () ->
                new GorgeousClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GRANDFATHER_CLOCK = registerWithMultiblockItem("grandfather_clock", () ->
                new GrandfatherClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 60.0F)) );
        public static final RegistryObject<Block> GREEN_CLOCK = registerWithItem("green_clock", () ->
                new GreenClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> LANTERN_CLOCK = registerWithItem("lantern_clock", () ->
                new LanternClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> LARGE_CLOCK_TOWER_DIAL = registerWithMultiblockItem("large_clock_tower_dial", () ->
                new LargeClockTowerDialBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 80.0F)) );
        public static final RegistryObject<Block> LIBRARY_CLOCK = registerWithMultiblockItem("library_clock", () ->
                new LibraryClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 60.0F)) );
        public static final RegistryObject<Block> MANTLE_CLOCK = registerWithItem("mantle_clock", () ->
                new MantleClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> MINIMALIST_CLOCK = registerWithItem("minimalist_clock", () ->
                new MinimalistClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> OWL_CLOCK = registerWithItem("owl_clock", () ->
                new OwlClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> RECOGNIZABLE_CLOCK = registerWithItem("recognizable_clock", () ->
                new RecognizableClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> RED_CLOCK = registerWithItem("red_clock", () ->
                new RedClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> REED_CLOCK = registerWithItem("reed_clock", () ->
                new ReedClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> REGAL_CLOCK = registerWithItem("regal_clock", () ->
                new RegalClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 12.0F)) );
        public static final RegistryObject<Block> ROCOCO_CLOCK = registerWithItem("rococo_clock", () ->
                new RococoClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 6.0F)) );
        public static final RegistryObject<Block> SLATE_CLOCK = registerWithItem("slate_clock", () ->
                new SlateClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 6.0F)) );
        public static final RegistryObject<Block> SMALL_CLOCK_TOWER_DIAL = registerWithMultiblockItem("small_clock_tower_dial", () ->
                new SmallClockTowerDialBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 60.0F)) );
        public static final RegistryObject<Block> STATION_CLOCK = registerWithMultiblockItem("station_clock", () ->
                new StationClockBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(3.5F, 60.0F)) );
        public static final RegistryObject<Block> WOODEN_BLOCK_CLOCK = registerWithItem("wooden_block_clock", () ->
                new WoodenBlockClockBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 4.0F)) );

        // STORAGE //
        public static final RegistryObject<Block> ANTIQUE_BOOKCASE = registerWithItem("antique_bookcase", () ->
                new AntiqueBookcaseBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> ANTIQUE_BUREAU = registerWithItem("antique_bureau", () ->
                new AntiqueBureauBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F)) );
        public static final RegistryObject<Block> ANTIQUE_CABINET = registerWithMultiblockItem("antique_cabinet", () ->
                new AntiqueCabinetBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F)) );
        public static final RegistryObject<Block> ANTIQUE_DESK = registerWithItem("antique_desk", () ->
                new AntiqueDeskBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> ANTIQUE_MIRROR = registerWithMultiblockItem("antique_mirror", () ->
                new AntiqueMirrorBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> ANTIQUE_WALL_SHELF = registerWithItem("antique_wall_shelf", () ->
                new AntiqueWallShelfBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> ANTIQUE_WARDROBE = registerWithMultiblockItem("antique_wardrobe", () ->
                new AntiqueWardrobeBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F)) );
        public static final RegistryObject<Block> BLUE_BOOKSHELF = registerWithItem("blue_bookshelf", () ->
                new BlueBookshelfBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> BLUE_BUREAU = registerWithItem("blue_bureau", () ->
                new BlueBureauBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> BLUE_CABINET = registerWithItem("blue_cabinet", () ->
                new BlueCabinetBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> BLUE_DRESSER = registerWithItem("blue_dresser", () ->
                new BlueDresserBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> BLUE_WARDROBE = registerWithItem("blue_wardrobe", () ->
                new BlueWardrobeBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> CABANA_BOOKCASE = registerWithItem("cabana_bookcase", () ->
                new CabanaBookcaseBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> CABANA_DRESSER = registerWithItem("cabana_dresser", () ->
                new CabanaDresserBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> CABANA_VANITY = registerWithItem("cabana_vanity", () ->
                new CabanaVanityBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> CABANA_WARDROBE = registerWithMultiblockItem("cabana_wardrobe", () ->
                new CabanaWardrobeBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F)) );
        public static final RegistryObject<Block> GORGEOUS_CHEST = registerWithItem("gorgeous_chest", () ->
                new GorgeousChestBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GORGEOUS_CLOSET = registerWithMultiblockItem("gorgeous_closet", () ->
                new GorgeousClosetBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F)) );
        public static final RegistryObject<Block> GORGEOUS_DESK = registerWithItem("gorgeous_desk", () ->
                new GorgeousDeskBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> GORGEOUS_MINI_DRAWER = registerWithItem("gorgeous_mini_drawer", () ->
                new GorgeousMiniDrawerBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> GREEN_DESK = registerWithItem("green_desk", () ->
                new GreenDeskBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> GREEN_DRESSER = registerWithItem("green_dresser", () ->
                new GreenDresserBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GREEN_MINI_DRAWER = registerWithItem("green_mini_drawer", () ->
                new GreenMiniDrawerBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> GREEN_PANTRY = registerWithItem("green_pantry", () ->
                new GreenPantryBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> GREEN_WARDROBE = registerWithItem("green_wardrobe", () ->
                new GreenWardrobeBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> MINIMALIST_DRESSER = registerWithItem("minimalist_dresser", () ->
                new MinimalistDresserBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> MINIMALIST_MIRROR = registerWithItem("minimalist_mirror", () ->
                new MinimalistMirrorBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> MINIMALIST_WARDROBE = registerWithItem("minimalist_wardrobe", () ->
                new MinimalistWardrobeBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> REGAL_ARMOIRE = registerWithItem("regal_armoire", () ->
                new RegalArmoireBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> REGAL_BOOKSHELF = registerWithMultiblockItem("regal_bookshelf", () ->
                new RegalBookshelfBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F)) );
        public static final RegistryObject<Block> REGAL_DRESSER = registerWithItem("regal_dresser", () ->
                new RegalDresserBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> REGAL_VANITY = registerWithItem("regal_vanity", () ->
                new RegalVanityBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> SWEETS_CLOSET = registerWithMultiblockItem("sweets_closet", () ->
                new SweetsClosetBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.5F, 30.0F)) );
        public static final RegistryObject<Block> SWEETS_DRESSER = registerWithMultiblockItem("sweets_dresser", () ->
                new SweetsDresserBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> WOODEN_BLOCK_DRAWERS = registerWithItem("wooden_block_drawers", () ->
                new WoodenBlockDrawersBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );

        // SEAT //
        public static final RegistryObject<Block> ANTIQUE_CHAIR = registerWithItem("antique_chair", () ->
                new AntiqueChairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> ANTIQUE_SOFA = registerWithItem("antique_sofa", () ->
                new AntiqueSofaBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> BLUE_BENCH = registerWithItem("blue_bench", () ->
                new BlueBenchBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> BLUE_CHAIR = registerWithItem("blue_chair", () ->
                new BlueChairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> CABANA_ARMCHAIR = registerWithItem("cabana_armchair", () ->
                new CabanaArmchairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> CABANA_CHAIR = registerWithItem("cabana_chair", () ->
                new CabanaChairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> EGYPTIAN_CHAIR = registerWithItem("egyptian_chair", () ->
                new EgyptianChairBlock(BlockBehaviour.Properties.of(Material.METAL).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GORGEOUS_SEAT = registerWithItem("gorgeous_seat", () ->
                new GorgeousSeatBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GORGEOUS_SOFA = registerWithItem("gorgeous_sofa", () ->
                new GorgeousSofaBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GORGEOUS_STOOL = registerWithItem("gorgeous_stool", () ->
                new GorgeousStoolBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> GREEN_BENCH = registerWithItem("green_bench", () ->
                new GreenBenchBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GREEN_CHAIR = registerWithItem("green_chair", () ->
                new GreenChairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> MINIMALIST_CHAIR = registerWithItem("minimalist_chair", () ->
                new MinimalistChairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> MINIMALIST_COUCH = registerWithItem("minimalist_couch", () ->
                new MinimalistCouchBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> MINIMALIST_STOOL = registerWithItem("minimalist_stool", () ->
                new MinimalistStoolBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> REGAL_CHAIR = registerWithItem("regal_chair", () ->
                new RegalChairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> REGAL_SOFA = registerWithItem("regal_sofa", () ->
                new RegalSofaBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> SWEETS_CHAIR = registerWithItem("sweets_chair", () ->
                new SweetsChairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> SWEETS_SOFA = registerWithItem("sweets_sofa", () ->
                new SweetsSofaBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> WOODEN_BLOCK_BENCH = registerWithItem("wooden_block_bench", () ->
                new WoodenBlockBenchBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> WOODEN_BLOCK_CHAIR = registerWithItem("wooden_block_chair", () ->
                new WoodenBlockChairBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> WOODEN_BLOCK_STOOL = registerWithItem("wooden_block_stool", () ->
                new WoodenBlockStoolBlock(BlockBehaviour.Properties.of(Material.WOOD).randomTicks().noOcclusion().strength(1.5F, 6.0F)) );

        // BED //
        public static final RegistryObject<Block> ANTIQUE_BED = registerWithMultiblockItem("antique_bed", () ->
                new AntiqueBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 30.0F)) );
        public static final RegistryObject<Block> BLUE_BED = registerWithMultiblockItem("blue_bed", () ->
                new BlueBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> CABANA_BED = registerWithMultiblockItem("cabana_bed", () ->
                new CabanaBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 30.0F)) );
        public static final RegistryObject<Block> EGYPTIAN_BED = registerWithMultiblockItem("egyptian_bed", () ->
                new EgyptianBedBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GREEN_BED = registerWithMultiblockItem("green_bed", () ->
                new GreenBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> GORGEOUS_BED = registerWithMultiblockItem("gorgeous_bed", () ->
                new GorgeousBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 30.0F)) );
        public static final RegistryObject<Block> MINIMALIST_BED = registerWithMultiblockItem("minimalist_bed", () ->
                new MinimalistBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> REGAL_BED = registerWithMultiblockItem("regal_bed", () ->
                new RegalBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 30.0F)) );
        public static final RegistryObject<Block> SWEETS_BED = registerWithMultiblockItem("sweets_bed", () ->
                new SweetsBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(3.0F, 30.0F)) );
        public static final RegistryObject<Block> WOODEN_BLOCK_BED = registerWithMultiblockItem("wooden_block_bed", () ->
                new WoodenBlockBedBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );

        // LIGHT //
        public static final RegistryObject<Block> ANTIQUE_WALL_OIL_LAMP = registerWithItem("antique_wall_oil_lamp", () ->
                new AntiqueWallOilLamp(BlockBehaviour.Properties.of(Material.METAL).lightLevel(b -> b.getValue(RotatingBlock.WATERLOGGED) ? 0 : 14).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> GORGEOUS_LAMP = registerWithItem("gorgeous_lamp", () ->
                new GorgeousLampBlock(BlockBehaviour.Properties.of(Material.WOOD).lightLevel(b -> 14).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> MINIMALIST_LAMP = registerWithItem("minimalist_lamp", () ->
                new MinimalistLampBlock(BlockBehaviour.Properties.of(Material.WOOD).lightLevel(b -> b.getValue(TallBlock.HALF) == DoubleBlockHalf.UPPER ? 14 : 0).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> NEON_CLUB_SIGN = registerWithItem("neon_club_sign", () ->
                new NeonSign(BlockBehaviour.Properties.of(Material.METAL).lightLevel(b -> 14).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> NEON_DIAMOND_SIGN = registerWithItem("neon_diamond_sign", () ->
                new NeonSign(BlockBehaviour.Properties.of(Material.METAL).lightLevel(b -> 14).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> NEON_HEART_SIGN = registerWithItem("neon_heart_sign", () ->
                new NeonSign(BlockBehaviour.Properties.of(Material.METAL).lightLevel(b -> 14).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> NEON_SPADE_SIGN = registerWithItem("neon_spade_sign", () ->
                new NeonSign(BlockBehaviour.Properties.of(Material.METAL).lightLevel(b -> 14).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> REGAL_LAMP = registerWithItem("regal_lamp", () ->
                new RegalLampBlock(BlockBehaviour.Properties.of(Material.WOOD).lightLevel(b -> 14).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> REGAL_WALL_LAMP = registerWithItem("regal_wall_lamp", () ->
                new RegalWallLampBlock(BlockBehaviour.Properties.of(Material.WOOD).lightLevel(b -> 14).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> SMALL_FIREPLACE = registerWithItem("small_fireplace", () ->
                new SmallFireplaceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_RED).lightLevel(b -> b.getValue(RotatingBlock.WATERLOGGED) ? 0 : 14).noOcclusion().strength(2.0F, 10.0F)) );


        // MISC //
        public static final RegistryObject<Block> DIY_WORKBENCH = registerWithItem("diy_workbench", () ->
                new DIYWorkbenchBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 30.0F)) );
        public static final RegistryObject<Block> HOLIDAY_TREE = registerWithItem("holiday_tree", () ->
                new HolidayTreeBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(2.0F, 10.0F)) );
        public static final RegistryObject<Block> HOURGLASS = registerWithItem("hourglass", () ->
                new HourglassBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> SHIP_IN_A_BOTTLE = registerWithItem("ship_in_a_bottle", () ->
                new ShipInABottleBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F, 6.0F)) );
        public static final RegistryObject<Block> TRAIN_SET = registerWithItem("train_set", () ->
                new TrainSetBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0F, 30.0F)) );



        // HELPER METHODS //

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
        public static final RegistryObject<BlockEntityType<ClockBlockEntity>> GINGERBREAD_CLOCK = registerClock(
                () -> BlockEntityReg.GINGERBREAD_CLOCK, BlockReg.GINGERBREAD_CLOCK);
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
                        BlockReg.ANTIQUE_BOOKCASE.get(), BlockReg.ANTIQUE_BUREAU.get(), BlockReg.ANTIQUE_DESK.get(), BlockReg.ANTIQUE_MIRROR.get(), BlockReg.ANTIQUE_WARDROBE.get(),
                        BlockReg.BLUE_BOOKSHELF.get(), BlockReg.BLUE_BUREAU.get(), BlockReg.BLUE_CABINET.get(), BlockReg.BLUE_WARDROBE.get(),
                        BlockReg.CABANA_BOOKCASE.get(), BlockReg.CABANA_DRESSER.get(), BlockReg.CABANA_VANITY.get(), BlockReg.CABANA_WARDROBE.get(),
                        BlockReg.GORGEOUS_CHEST.get(), BlockReg.GORGEOUS_CLOSET.get(),
                        BlockReg.GREEN_DRESSER.get(), BlockReg.GREEN_WARDROBE.get(),
                        BlockReg.MINIMALIST_DRESSER.get(), BlockReg.MINIMALIST_MIRROR.get(), BlockReg.MINIMALIST_WARDROBE.get(),
                        BlockReg.REGAL_ARMOIRE.get(), BlockReg.REGAL_BOOKSHELF.get(), BlockReg.REGAL_DRESSER.get(), BlockReg.REGAL_VANITY.get(),
                        BlockReg.SWEETS_CLOSET.get(), BlockReg.SWEETS_DRESSER.get(),
                        BlockReg.WOODEN_BLOCK_DRAWERS.get(),
                        BlockReg.DIY_WORKBENCH.get())
                .build(null));

        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_BOOKCASE = registerStorage(
                () -> BlockEntityReg.ANTIQUE_BOOKCASE, 3, BlockReg.ANTIQUE_BOOKCASE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_BUREAU = registerStorage(
                () -> BlockEntityReg.ANTIQUE_BUREAU, 3, BlockReg.ANTIQUE_BUREAU);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_CABINET = registerStorage(
                () -> BlockEntityReg.ANTIQUE_CABINET, 6, BlockReg.ANTIQUE_CABINET);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_DESK = registerStorage(
                () -> BlockEntityReg.ANTIQUE_DESK, 3, BlockReg.ANTIQUE_DESK);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_MIRROR = registerStorage(
                () -> BlockEntityReg.ANTIQUE_MIRROR, 6, BlockReg.ANTIQUE_MIRROR);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_WARDROBE = registerStorage(
                () -> BlockEntityReg.ANTIQUE_WARDROBE, 6, BlockReg.ANTIQUE_WARDROBE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> ANTIQUE_WALL_SHELF = registerStorage(
                () -> BlockEntityReg.ANTIQUE_WALL_SHELF, 3, BlockReg.ANTIQUE_WALL_SHELF);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> BLUE_BOOKSHELF = registerStorage(
                () -> BlockEntityReg.BLUE_BOOKSHELF, 3, BlockReg.BLUE_BOOKSHELF);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> BLUE_BUREAU = registerStorage(
                () -> BlockEntityReg.BLUE_BUREAU, 6, BlockReg.BLUE_BUREAU);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> BLUE_CABINET = registerStorage(
                () -> BlockEntityReg.BLUE_CABINET, 6, BlockReg.BLUE_CABINET);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> BLUE_DRESSER = registerStorage(
                () -> BlockEntityReg.BLUE_DRESSER, 3, BlockReg.BLUE_DRESSER);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> BLUE_WARDROBE = registerStorage(
                () -> BlockEntityReg.BLUE_WARDROBE, 6, BlockReg.BLUE_WARDROBE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> CABANA_BOOKCASE = registerStorage(
                () -> BlockEntityReg.CABANA_BOOKCASE, 3, BlockReg.CABANA_BOOKCASE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> CABANA_DRESSER = registerStorage(
                () -> BlockEntityReg.CABANA_DRESSER, 6, BlockReg.CABANA_DRESSER);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> CABANA_VANITY = registerStorage(
                () -> BlockEntityReg.CABANA_VANITY, 3, BlockReg.CABANA_VANITY);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> CABANA_WARDROBE = registerStorage(
                () -> BlockEntityReg.CABANA_WARDROBE, 6, BlockReg.CABANA_WARDROBE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GORGEOUS_CHEST = registerStorage(
                () -> BlockEntityReg.GORGEOUS_CHEST, 6, BlockReg.GORGEOUS_CHEST);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GORGEOUS_CLOSET = registerStorage(
                () -> BlockEntityReg.GORGEOUS_CLOSET, 6, BlockReg.GORGEOUS_CLOSET);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GORGEOUS_DESK = registerStorage(
                () -> BlockEntityReg.GORGEOUS_DESK, 3, BlockReg.GORGEOUS_DESK);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GORGEOUS_MINI_DRAWER = registerStorage(
                () -> BlockEntityReg.GORGEOUS_MINI_DRAWER, 3, BlockReg.GORGEOUS_MINI_DRAWER);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GREEN_DESK = registerStorage(
                () -> BlockEntityReg.GREEN_DESK, 3, BlockReg.GREEN_DESK);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GREEN_DRESSER = registerStorage(
                () -> BlockEntityReg.GREEN_DRESSER, 6, BlockReg.GREEN_DRESSER);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GREEN_MINI_DRAWER = registerStorage(
                () -> BlockEntityReg.GREEN_MINI_DRAWER, 3, BlockReg.GREEN_MINI_DRAWER);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GREEN_PANTRY = registerStorage(
                () -> BlockEntityReg.GREEN_PANTRY, 3, BlockReg.GREEN_PANTRY);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> GREEN_WARDROBE = registerStorage(
                () -> BlockEntityReg.GREEN_WARDROBE, 6, BlockReg.GREEN_WARDROBE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> MINIMALIST_DRESSER = registerStorage(
                () -> BlockEntityReg.MINIMALIST_DRESSER, 6, BlockReg.MINIMALIST_DRESSER);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> MINIMALIST_MIRROR = registerStorage(
                () -> BlockEntityReg.MINIMALIST_MIRROR, 3, BlockReg.MINIMALIST_MIRROR);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> MINIMALIST_WARDROBE = registerStorage(
                () -> BlockEntityReg.MINIMALIST_WARDROBE, 6, BlockReg.MINIMALIST_WARDROBE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> REGAL_ARMOIRE = registerStorage(
                () -> BlockEntityReg.REGAL_ARMOIRE, 6, BlockReg.REGAL_ARMOIRE);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> REGAL_BOOKSHELF = registerStorage(
                () -> BlockEntityReg.REGAL_BOOKSHELF, 6, BlockReg.REGAL_BOOKSHELF);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> REGAL_DRESSER = registerStorage(
                () -> BlockEntityReg.REGAL_DRESSER, 6, BlockReg.REGAL_DRESSER);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> REGAL_VANITY = registerStorage(
                () -> BlockEntityReg.REGAL_VANITY, 3, BlockReg.REGAL_VANITY);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> SWEETS_CLOSET = registerStorage(
                () -> BlockEntityReg.SWEETS_CLOSET, 6, BlockReg.SWEETS_CLOSET);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> SWEETS_DRESSER = registerStorage(
                () -> BlockEntityReg.SWEETS_DRESSER, 6, BlockReg.SWEETS_DRESSER);
        public static final RegistryObject<BlockEntityType<StorageBlockEntity>> WOODEN_BLOCK_DRAWERS = registerStorage(
                () -> BlockEntityReg.WOODEN_BLOCK_DRAWERS, 6, BlockReg.WOODEN_BLOCK_DRAWERS);

        // MISC //
        public static final RegistryObject<BlockEntityType<DIYWorkbenchBlockEntity>> DIY_WORKBENCH = BLOCK_ENTITY_TYPES.register("diy_workbench", () -> BlockEntityType.Builder
                .of((pos, state) -> new DIYWorkbenchBlockEntity(BlockEntityReg.DIY_WORKBENCH.get(), pos, state), BlockReg.DIY_WORKBENCH.get())
                .build(null));
        public static final RegistryObject<BlockEntityType<HourglassBlockEntity>> HOURGLASS = BLOCK_ENTITY_TYPES.register("hourglass", () -> BlockEntityType.Builder
                .of((pos, state) -> new HourglassBlockEntity(BlockEntityReg.HOURGLASS.get(), pos, state), BlockReg.HOURGLASS.get())
                .build(null));
        public static final RegistryObject<BlockEntityType<TrainSetBlockEntity>> TRAIN_SET = BLOCK_ENTITY_TYPES.register("train_set", () -> BlockEntityType.Builder
                .of((pos, state) -> new TrainSetBlockEntity(BlockEntityReg.TRAIN_SET.get(), pos, state), BlockReg.TRAIN_SET.get())
                .build(null));

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
        public static final RegistryObject<SoundEvent> MEDIUM_CLOCK_CHIME2 = register("block.medium_clock.chime2");
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

    public static final class RecipeReg {

        private static void register() {
            RECIPE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
            RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final RegistryObject<RecipeSerializer<DIYRecipe>> DIY_SERIALIZER = RECIPE_SERIALIZERS.register(DIYRecipe.Serializer.CATEGORY, () -> new DIYRecipe.Serializer());

        public static final RegistryObject<RecipeType<DIYRecipe>> DIY = RECIPE_TYPES.register(DIYRecipe.Serializer.CATEGORY, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return DIYRecipe.Serializer.CATEGORY;
            }
        });
    }

    public static final class MenuReg {

        private static void register() {
            MENU_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public static final RegistryObject<MenuType<DIYWorkbenchMenu>> DIY_WORKBENCH = MENU_TYPES.register("diy_workbench", () ->
                IForgeMenuType.create((windowId, inv, data) -> {
                    BlockPos pos = data.readBlockPos();
                    Container container = (Container) inv.player.level.getBlockEntity(pos);
                    return new DIYWorkbenchMenu(windowId, inv, pos, container);
                })
        );
    }
}
