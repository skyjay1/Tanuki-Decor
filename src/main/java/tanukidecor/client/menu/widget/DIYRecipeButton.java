/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.menu.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import tanukidecor.block.recipe.DIYRecipe;
import tanukidecor.client.menu.DIYWorkbenchScreen;

public class DIYRecipeButton extends ImageButton {

    public static final int WIDTH = 143;
    public static final int HEIGHT = 18;

    protected final ItemRenderer itemRenderer;
    protected final Font fontRenderer;

    protected ItemStack itemStack;
    protected DIYRecipe recipe;

    public DIYRecipeButton(int pX, int pY, final ItemRenderer itemRenderer, final Font fontRenderer, OnPress pOnPress) {
        super(pX, pY, WIDTH, HEIGHT, 0, 216, HEIGHT, DIYWorkbenchScreen.TEXTURE, 256, 256, pOnPress, Component.empty());
        this.itemRenderer = itemRenderer;
        this.fontRenderer = fontRenderer;
        this.recipe = null;
        this.itemStack = ItemStack.EMPTY;
        this.packedFGColor = 0x404040;
        this.setTooltip(null);
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public DIYRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(final DIYRecipe recipe) {
        this.recipe = recipe;
        this.itemStack = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        final Component hoverName = this.itemStack.getHoverName();
        final String sMessage = StringUtil.truncateStringIfNecessary(hoverName.getString(), (int)((this.getWidth() - 16 - 6) / 4.5F), true);
        this.setMessage(Component.literal(sMessage).withStyle(hoverName.getStyle()));
        this.setTooltip(DIYWorkbenchScreen.createTooltip(Screen.getTooltipFromItem(Minecraft.getInstance(), this.itemStack)));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTexture(guiGraphics, this.resourceLocation, this.getX(), this.getY(), this.xTexStart, this.yTexStart, this.yDiffTex, this.width, this.height, this.textureWidth, this.textureHeight);
        if(!this.itemStack.isEmpty()) {
            int x = this.getX() + 2;
            int y = this.getY() + (this.height - 16) / 2;
            // render item
            guiGraphics.renderFakeItem(this.itemStack, x, y);
            // render text
            renderString(guiGraphics, this.fontRenderer, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }

    @Override
    public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor) {
        pGuiGraphics.drawString(this.fontRenderer, this.getMessage().getVisualOrderText(), this.getX() + 16 + 4, this.getY() + (this.getHeight() - this.fontRenderer.lineHeight) / 2, 0x404040, false);
    }


}
