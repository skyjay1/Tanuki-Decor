/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.menu;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import tanukidecor.TDRegistry;
import tanukidecor.TanukiDecor;
import tanukidecor.block.recipe.DIYRecipe;
import tanukidecor.client.menu.widget.DIYRecipeButton;
import tanukidecor.client.menu.widget.ScrollButton;
import tanukidecor.menu.DIYWorkbenchMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DIYWorkbenchScreen extends AbstractContainerScreen<DIYWorkbenchMenu> implements ScrollButton.IScrollListener {

    public static final ResourceLocation TEXTURE = new ResourceLocation(TanukiDecor.MODID, "textures/gui/diy_workbench.png");

    public static final int WIDTH = 182;
    public static final int HEIGHT = 216;

    private static final int SEARCH_X = 57;
    private static final int SEARCH_Y = 35;
    private static final int SEARCH_WIDTH = 96;
    private static final int SEARCH_HEIGHT = 10;

    private static final int RECIPE_BUTTON_COUNT_Y = 4;
    private static final int RECIPE_X = 11;
    private static final int RECIPE_Y = 49;

    private static final int SCROLL_X = 159;
    private static final int SCROLL_Y = RECIPE_Y;
    private static final int SCROLL_HEIGHT = 72;

    private final Map<String, DIYRecipe> recipes;

    private final List<Map.Entry<String, DIYRecipe>> sortedRecipes;
    private final List<DIYRecipeButton> recipeButtons;
    private ScrollButton scrollButton;
    private EditBox editBox;
    private int scrollOffset;
    private Component resultCountText;

    public DIYWorkbenchScreen(DIYWorkbenchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        // prepare data
        this.recipes = populateRecipes();
        this.sortedRecipes = new ArrayList<>(RECIPE_BUTTON_COUNT_Y);
        this.recipeButtons = new ArrayList<>(RECIPE_BUTTON_COUNT_Y);
        // prepare screen
        this.imageWidth = WIDTH;
        this.imageHeight = HEIGHT;
        this.inventoryLabelX = DIYWorkbenchMenu.PLAYER_INV_X;
        this.inventoryLabelY = DIYWorkbenchMenu.PLAYER_INV_Y - 11;
        this.titleLabelY = 5;
    }

    //// INIT ////

    @Override
    protected void init() {
        super.init();
        // scroll button
        this.scrollButton = this.addRenderableWidget(new ScrollButton(this.leftPos + SCROLL_X, this.topPos + SCROLL_Y, 12, SCROLL_HEIGHT,
                TEXTURE, 244, 0, 12, 15, 15, true, 1.0F, this));
        // edit box
        this.editBox = this.addRenderableWidget(new EditBox(this.font, this.leftPos + SEARCH_X, this.topPos + SEARCH_Y, SEARCH_WIDTH, SEARCH_HEIGHT, Component.translatable("container.search")));
        this.editBox.setCanLoseFocus(false);
        this.editBox.setTextColor(-1);
        this.editBox.setTextColorUneditable(-1);
        this.editBox.setBordered(false);
        this.editBox.setMaxLength(70);
        this.editBox.setValue("");
        this.editBox.setResponder(s -> updateRecipes(s));
        this.setInitialFocus(this.editBox);
        //this.editBox.setEditable(false);
        // recipe buttons
        final Button.OnPress recipeButtonOnPress = b -> getMenu().selectRecipe(((DIYRecipeButton)b).getRecipe());
        final ItemRenderer itemRenderer = this.getMinecraft().getItemRenderer();
        for(int i = 0, x = this.leftPos + RECIPE_X, y = this.topPos + RECIPE_Y; i < RECIPE_BUTTON_COUNT_Y; i++) {
            this.recipeButtons.add(this.addRenderableWidget(new DIYRecipeButton(x, y + i * DIYRecipeButton.HEIGHT, itemRenderer, this.font, recipeButtonOnPress)));
        }
        updateRecipes(editBox.getValue());
        updateRecipeButtons();
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = this.editBox.getValue();
        this.init(pMinecraft, pWidth, pHeight);
        this.editBox.setValue(s);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) {
            this.getMenu().getInventory().player.closeContainer();
        }

        return this.editBox.keyPressed(pKeyCode, pScanCode, pModifiers) || this.editBox.canConsumeInput() || super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.editBox.tick();
    }

    //// RENDER ////

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.enableDepthTest();
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // render background
        this.renderBackground(guiGraphics);
        // render labels and widgets
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // render result count text
        guiGraphics.drawString(this.font, resultCountText.getVisualOrderText(), this.leftPos + SEARCH_X + SEARCH_WIDTH + 4, this.topPos + SEARCH_Y + (SEARCH_HEIGHT - font.lineHeight) / 2, 0x404040, false);
        // render tooltips
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    //// RECIPES ////

    private Map<String, DIYRecipe> populateRecipes() {
        if(!TanukiDecor.CONFIG.isDIYWorkbenchEnabled.get()) {
            return ImmutableMap.of();
        }
        // create map builder
        final ImmutableMap.Builder<String, DIYRecipe> builder = ImmutableMap.builder();
        // add all recipes with the name of the result item as the key
        final Collection<DIYRecipe> recipes = getMenu().getInventory().player.level().getRecipeManager().getAllRecipesFor(TDRegistry.RecipeReg.DIY.get());
        for(DIYRecipe recipe : recipes) {
            builder.put(recipe.getResultItem(getMenu().getRegistryAccess()).getHoverName().getString().toLowerCase(Locale.ENGLISH), recipe);
        }
        return builder.build();
    }

    private void updateRecipes(final String filter) {
        this.sortedRecipes.clear();
        if(!(TanukiDecor.CONFIG.isDIYWorkbenchEnabled.get())) {
            return;
        }
        if(filter.isEmpty()) {
            // add all recipes
            this.sortedRecipes.addAll(this.recipes.entrySet());
        } else {
            // add matching recipes
            final String searchTerm = filter.toLowerCase(Locale.ENGLISH);
            this.sortedRecipes.addAll(this.recipes.entrySet()
                    .stream()
                    .filter(e -> e.getKey().contains(searchTerm))
                    .toList());
        }
        // sort by name
        this.sortedRecipes.sort(Map.Entry.comparingByKey());
        // update scroll bar
        this.scrollButton.setScrollAmountMultiplier(1.0F / Math.max(1, sortedRecipes.size() - RECIPE_BUTTON_COUNT_Y));
        this.scrollButton.setScrollPercent(0);
        this.scrollButton.active = this.sortedRecipes.size() > RECIPE_BUTTON_COUNT_Y;
        // update count text
        this.resultCountText = Component.literal("" + sortedRecipes.size());
        // update recipe buttons
        this.updateRecipeButtons();
    }

    private void updateRecipeButtons() {
        for(int i = 0, n = recipeButtons.size(); i < n; i++) {
            DIYRecipeButton button = recipeButtons.get(i);
            int index = i + scrollOffset;
            if(index < 0 || index >= sortedRecipes.size()) {
                button.visible = button.active = false;
                continue;
            }
            button.visible = button.active = true;
            button.setRecipe(sortedRecipes.get(index).getValue());
        }
    }

    //// SCROLL LISTENER ////

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if(isHovering(RECIPE_X, RECIPE_Y, DIYRecipeButton.WIDTH, DIYRecipeButton.HEIGHT * RECIPE_BUTTON_COUNT_Y, pMouseX, pMouseY)) {
            return scrollButton.mouseScrolled(pMouseX, pMouseY, pDelta);
        }
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(button == 0 && scrollButton != null && scrollButton.isDragging()) {
            scrollButton.onDrag(mouseX, mouseY, dragX, dragY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void onScroll(ScrollButton button, float percent) {
        this.scrollOffset = Mth.floor(Math.max(0, percent * Math.max(0, sortedRecipes.size() - RECIPE_BUTTON_COUNT_Y)));
        updateRecipeButtons();
    }

    //// TOOLTIP ////

    public static Tooltip createTooltip(final List<Component> list) {
        MutableComponent component = Component.empty();
        // add each component, separated by a newline
        for(Component c : list) {
            component.getSiblings().add(c);
            component.getSiblings().add(Component.literal("\n"));
        }
        // remove trailing newline
        if(component.getSiblings().size() > 1) {
            component.getSiblings().remove(component.getSiblings().size() - 1);
        }
        // create the tooltip
        return Tooltip.create(component);
    }
}
