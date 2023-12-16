/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.menu.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import tanukidecor.block.recipe.DIYRecipe;
import tanukidecor.client.menu.DIYWorkbenchScreen;

public class DIYRecipeButton extends ImageButton {

    public static final int WIDTH = 143;
    public static final int HEIGHT = 18;

    public static final int TEXT_WIDTH = WIDTH - 4;

    protected final ItemRenderer itemRenderer;
    protected final Font fontRenderer;

    protected ItemStack itemStack;
    protected DIYRecipe recipe;
    protected float textScale;

    public DIYRecipeButton(int pX, int pY, final ItemRenderer itemRenderer, final Font fontRenderer, OnPress pOnPress) {
        super(pX, pY, WIDTH, HEIGHT, 0, 216, DIYWorkbenchScreen.TEXTURE, pOnPress);
        this.itemRenderer = itemRenderer;
        this.fontRenderer = fontRenderer;
        this.recipe = null;
        this.itemStack = ItemStack.EMPTY;
    }

    public DIYRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(final DIYRecipe recipe) {
        this.recipe = recipe;
        this.itemStack = recipe.getResultItem();
        this.setMessage(this.itemStack.getHoverName());
        // determine text scale
        this.textScale = 1.0F;
        if(this.fontRenderer.width(this.getMessage()) > TEXT_WIDTH) {
            this.textScale = 0.5F;
        }
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
        if(!this.itemStack.isEmpty()) {
            int x = this.x + 2;
            int y = this.y + (this.height - 16) / 2;
            // render item
            this.itemRenderer.renderGuiItem(this.itemStack, x, y);
            // render text
            x += 16 + 2;
            y = this.y + (this.fontRenderer.lineHeight) / 2;
            pPoseStack.pushPose();
            pPoseStack.scale(textScale, textScale, textScale);
            this.fontRenderer.drawWordWrap(this.getMessage(), (int) (x / textScale), (int) (y / textScale), (int) (TEXT_WIDTH / textScale), 0);
            pPoseStack.popPose();
        }
    }
}
