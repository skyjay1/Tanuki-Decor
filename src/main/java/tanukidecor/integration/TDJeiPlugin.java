/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import tanukidecor.TDRegistry;
import tanukidecor.TanukiDecor;
import tanukidecor.recipe.DIYRecipe;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@mezz.jei.api.JeiPlugin
public class TDJeiPlugin implements IModPlugin {

    public static final ResourceLocation UID = new ResourceLocation(TanukiDecor.MODID, "jei");

    private static final TagKey<Item> DIY_BLACKLIST_TAG_KEY = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(TanukiDecor.MODID, "diy_blacklist"));

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JeiDIYRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final List<DIYRecipe> recipes = Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(TDRegistry.RecipeReg.DIY.get())
                .stream()
                .filter(recipe -> !recipe.getResultItem().is(DIY_BLACKLIST_TAG_KEY))
                .toList();
        registration.addRecipes(JeiDIYRecipeCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TDRegistry.BlockReg.DIY_WORKBENCH.get()), JeiDIYRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        final Collection<ItemStack> blacklistItems = ForgeRegistries.ITEMS.tags().getTag(DIY_BLACKLIST_TAG_KEY)
                .stream()
                .map(ItemStack::new)
                .collect(Collectors.toSet());
        if(!blacklistItems.isEmpty()) {
            jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, blacklistItems);
        }
    }
}
