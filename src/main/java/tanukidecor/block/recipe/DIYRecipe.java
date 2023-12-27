/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import tanukidecor.TDRegistry;
import tanukidecor.block.entity.DIYWorkbenchBlockEntity;

public class DIYRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final ItemStack result;

    public DIYRecipe(ResourceLocation id, ItemStack result) {
        this.id = id;
        this.result = result;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        for(int i = 0; i < 4; i++) {
            Ingredient ingredient = DIYWorkbenchBlockEntity.INGREDIENTS[i];
            ItemStack itemStack = pContainer.getItem(i);
            if(!ingredient.test(itemStack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return (pWidth * pHeight) >= 4;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TDRegistry.RecipeReg.DIY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TDRegistry.RecipeReg.DIY.get();
    }

    //// SERIALIZER ////

    public static class Serializer implements RecipeSerializer<DIYRecipe> {

        public static final String CATEGORY = "diy";
        private static final String KEY_RESULT = "result";

        @Override
        public DIYRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            final ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, KEY_RESULT));
            return new DIYRecipe(recipeId, result);
        }

        @Override
        public DIYRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            final ItemStack result = buffer.readItem();
            return new DIYRecipe(recipeId, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DIYRecipe recipe) {
            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
