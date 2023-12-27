/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.menu.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import tanukidecor.block.recipe.DIYRecipe;
import tanukidecor.client.menu.DIYWorkbenchScreen;

import java.util.List;

public class DIYRecipeButton extends ImageButton {

    public static final int WIDTH = 143;
    public static final int HEIGHT = 18;

    public static final int TEXT_WIDTH = WIDTH - 4;

    protected final ItemRenderer itemRenderer;
    protected final Font fontRenderer;

    protected ItemStack itemStack;
    protected DIYRecipe recipe;
    protected Component text;

    public DIYRecipeButton(int pX, int pY, final ItemRenderer itemRenderer, final Font fontRenderer,
                           OnPress pOnPress, OnTooltip onTooltip) {
        super(pX, pY, WIDTH, HEIGHT, 0, 216, HEIGHT, DIYWorkbenchScreen.TEXTURE, 256, 256, pOnPress, onTooltip, new TextComponent(""));
        this.itemRenderer = itemRenderer;
        this.fontRenderer = fontRenderer;
        this.recipe = null;
        this.itemStack = ItemStack.EMPTY;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public DIYRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(final DIYRecipe recipe) {
        this.recipe = recipe;
        this.itemStack = recipe.getResultItem();
        this.setMessage(this.itemStack.getHoverName());
        final String message = StringUtil.truncateStringIfNecessary(this.itemStack.getHoverName().getString(), (int)(TEXT_WIDTH / 5.5F), true);
        this.text = new TextComponent(message).withStyle(this.getMessage().getStyle());
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
            this.fontRenderer.draw(pPoseStack, this.text, x, y, 0x404040);
        }
    }
}
