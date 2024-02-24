/*
 * Copyright (c) 2024 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.searchtree.MutableSearchTree;
import net.minecraft.client.searchtree.ReloadableSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import tanukidecor.TDRegistry;

import java.util.ArrayList;
import java.util.List;

public final class ClientRecipeCollections {

    public static final SearchRegistry.Key<RecipeCollection> DIY_RECIPE_COLLECTIONS_KEY = new SearchRegistry.Key<>();
    public static final List<RecipeCollection> DIY_RECIPE_COLLECTIONS = new ArrayList<>();

    private ClientRecipeCollections() {}

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientRecipeCollections::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(ClientRecipeCollections::onUpdateRecipes);
        MinecraftForge.EVENT_BUS.addListener(ClientRecipeCollections::onUpdateTags);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ClientRecipeCollections::registerSearchTrees);
    }

    /**
     * Registers a search tree of {@link RecipeCollection}s that is indexed by item names
     **/
    private static void registerSearchTrees() {
        // create a search tree (copied from Minecraft#createSearchTrees)
        final ReloadableSearchTree<RecipeCollection> searchTree = new ReloadableSearchTree<>((recipes) ->
                recipes.getRecipes()
                        .stream()
                        .flatMap((recipe) -> recipe.getResultItem().getTooltipLines(null, TooltipFlag.Default.NORMAL).stream())
                        .map((component) -> ChatFormatting.stripFormatting(component.getString()).trim())
                        .filter((s) -> !s.isEmpty()), (collection) -> collection.getRecipes()
                .stream()
                .map((recipe) -> ForgeRegistries.ITEMS.getKey(recipe.getResultItem().getItem())));
        // register search tree for DIY recipes
        Minecraft.getInstance().getSearchTreeManager().register(ClientRecipeCollections.DIY_RECIPE_COLLECTIONS_KEY, searchTree);
    }

    /**
     * Populates the recipe cache and search tree with {@link RecipeCollection}s that contain
     * one {@link tanukidecor.recipe.DIYRecipe} each
     * @param event the recipe update event
     */
    private static void onUpdateRecipes(final RecipesUpdatedEvent event) {
        // load search tree
        MutableSearchTree<RecipeCollection> searchTree = Minecraft.getInstance().getSearchTree(DIY_RECIPE_COLLECTIONS_KEY);
        // clear old recipes
        DIY_RECIPE_COLLECTIONS.clear();
        searchTree.clear();
        // add updated recipes to search tree, one recipe per RecipeCollection
        event.getRecipeManager()
                .getAllRecipesFor(TDRegistry.RecipeReg.DIY.get())
                .stream()
                .map(recipe -> new RecipeCollection(ImmutableList.of(recipe)))
                .forEach(recipeCollection -> {
                    DIY_RECIPE_COLLECTIONS.add(recipeCollection);
                    searchTree.add(recipeCollection);
                });
        // refresh search tree
        searchTree.refresh();
    }

    /**
     * Filters the recipe cache and search tree to remove any blacklisted items
     * @param event the tag update event
     */
    private static void onUpdateTags(final TagsUpdatedEvent event) {
        // load search tree
        MutableSearchTree<RecipeCollection> searchTree = Minecraft.getInstance().getSearchTree(DIY_RECIPE_COLLECTIONS_KEY);
        // clear old recipes
        final List<RecipeCollection> recipes = ImmutableList.copyOf(DIY_RECIPE_COLLECTIONS);
        DIY_RECIPE_COLLECTIONS.clear();
        searchTree.clear();
        // add all recipes that are not blacklisted back into the list and search tree
        for(RecipeCollection recipeCollection : recipes) {
            if(recipeCollection.getRecipes().size() == 1 && !recipeCollection.getRecipes().get(0).getResultItem().is(TDRegistry.DIY_BLACKLIST_TAG_KEY)) {
                DIY_RECIPE_COLLECTIONS.add(recipeCollection);
                searchTree.add(recipeCollection);
            }
        }
        // refresh search tree
        searchTree.refresh();
    }
}
