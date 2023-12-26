/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tanukidecor.TDRegistry;
import tanukidecor.TanukiDecor;
import tanukidecor.block.recipe.DIYRecipe;

import java.util.List;

@mezz.jei.api.JeiPlugin
public class TDJeiPlugin implements IModPlugin {

    public static final ResourceLocation UID = new ResourceLocation(TanukiDecor.MODID, "jei");

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
        final List<DIYRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TDRegistry.RecipeReg.DIY.get());
        registration.addRecipes(JeiDIYRecipeCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TDRegistry.BlockReg.DIY_WORKBENCH.get()), JeiDIYRecipeCategory.RECIPE_TYPE);
    }
}
