/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.menu;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Recipe;
import tanukidecor.TDRegistry;
import tanukidecor.TanukiDecor;
import tanukidecor.client.ClientRecipeCollections;
import tanukidecor.client.menu.widget.DIYRecipeButton;
import tanukidecor.client.menu.widget.ScrollButton;
import tanukidecor.menu.DIYWorkbenchMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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

    private static final Comparator<RecipeCollection> SORT_RECIPES_BY_RESULT_NAME = Comparator.comparing(recipeCollection -> recipeCollection.getRecipes().isEmpty() ? "" : recipeCollection.getRecipes().get(0).getResultItem().getHoverName().getString());

    private final Collection<RecipeCollection> recipes;
    private final SearchTree<RecipeCollection> searchTree;
    private final List<RecipeCollection> sortedRecipes;

    private final List<DIYRecipeButton> recipeButtons;
    private ScrollButton scrollButton;
    private EditBox editBox;
    private int scrollOffset;
    private Component resultCountText;

    public DIYWorkbenchScreen(DIYWorkbenchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        // collect recipes, excluding any that result in a blacklisted item
        final ImmutableList.Builder<RecipeCollection> recipesBuilder = ImmutableList.builder();
        for(RecipeCollection recipeCollection : ClientRecipeCollections.DIY_RECIPE_COLLECTIONS) {
            if(recipeCollection.getRecipes().size() == 1 && !recipeCollection.getRecipes().get(0).getResultItem().is(TDRegistry.DIY_BLACKLIST_TAG_KEY)) {
                recipesBuilder.add(recipeCollection);
            }
        }
        this.recipes = ImmutableList.sortedCopyOf(SORT_RECIPES_BY_RESULT_NAME, recipesBuilder.build());
        // prepare search tree and recipe slice
        this.searchTree = Minecraft.getInstance().getSearchTree(ClientRecipeCollections.DIY_RECIPE_COLLECTIONS_KEY);
        this.sortedRecipes = new ArrayList<>(this.recipes.size());
        // prepare button lists
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
        this.scrollOffset = 0;
        // edit box
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        this.editBox = this.addRenderableWidget(new EditBox(this.font, this.leftPos + SEARCH_X, this.topPos + SEARCH_Y, SEARCH_WIDTH, SEARCH_HEIGHT, new TranslatableComponent("container.search")));
        this.editBox.setCanLoseFocus(false);
        this.editBox.setTextColor(-1);
        this.editBox.setTextColorUneditable(-1);
        this.editBox.setBordered(false);
        this.editBox.setMaxLength(70);
        this.editBox.setValue("");
        this.editBox.setResponder(s -> updateRecipes(s));
        this.setInitialFocus(this.editBox);
        // recipe buttons
        this.recipeButtons.clear();
        final Button.OnPress recipeButtonOnPress = b -> getMenu().selectRecipe(((DIYRecipeButton)b).getRecipe());
        final Button.OnTooltip recipeButtonOnTooltip = (b, p, mx, my) -> renderTooltip(p, ((DIYRecipeButton)b).getItemStack(), mx, my);
        for(int i = 0, x = this.leftPos + RECIPE_X, y = this.topPos + RECIPE_Y; i < RECIPE_BUTTON_COUNT_Y; i++) {
            this.recipeButtons.add(this.addRenderableWidget(new DIYRecipeButton(x, y + i * DIYRecipeButton.HEIGHT, this.itemRenderer, this.font, recipeButtonOnPress, recipeButtonOnTooltip)));
        }
        updateRecipes("");
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = this.editBox.getValue();
        super.resize(pMinecraft, pWidth, pHeight);
        this.editBox.setValue(s);
    }

    @Override
    public void removed() {
        super.removed();
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256) {
            this.getMenu().getInventory().player.closeContainer();
        }
        return this.editBox.keyPressed(pKeyCode, pScanCode, pModifiers) || this.editBox.canConsumeInput() || super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    //// RENDER ////

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        // render background
        this.renderBackground(pPoseStack);
        // render labels and widgets
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        // render result count text
        this.font.draw(pPoseStack, resultCountText, this.leftPos + SEARCH_X + SEARCH_WIDTH + 4, this.topPos + SEARCH_Y + (SEARCH_HEIGHT - font.lineHeight) / 2.0F, 0x404040);
        // render tooltips
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    //// RECIPES ////

    private void updateRecipes(final String filter) {
        this.sortedRecipes.clear();
        if(!(TanukiDecor.CONFIG.isDIYWorkbenchEnabled.get())) {
            return;
        }
        if(filter.isEmpty()) {
            this.sortedRecipes.addAll(this.recipes);
        } else {
            this.sortedRecipes.addAll(this.searchTree.search(filter.toLowerCase(Locale.ROOT)));
        }
        // update scroll bar
        this.scrollButton.setScrollAmountMultiplier(1.0F / Math.max(1, sortedRecipes.size() - RECIPE_BUTTON_COUNT_Y));
        this.scrollButton.active = this.sortedRecipes.size() > RECIPE_BUTTON_COUNT_Y;
        // update count text
        this.resultCountText = new TextComponent("" + sortedRecipes.size());
        // update recipe buttons
        this.updateRecipeButtons();
    }

    private void updateRecipeButtons() {
        for(int i = 0, n = recipeButtons.size(); i < n; i++) {
            DIYRecipeButton button = recipeButtons.get(i);
            int index = i + scrollOffset;
            if(index < 0 || index >= sortedRecipes.size() || sortedRecipes.get(index).getRecipes().isEmpty()) {
                button.visible = button.active = false;
                continue;
            }
            button.visible = button.active = true;
            button.setRecipe(sortedRecipes.get(index).getRecipes().get(0));
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
        final int oldScrollOffset = this.scrollOffset;
        this.scrollOffset = Mth.floor(Math.max(0, percent * Math.max(0, sortedRecipes.size() - RECIPE_BUTTON_COUNT_Y)));
        if(oldScrollOffset != scrollOffset) {
            updateRecipeButtons();
        }
    }
}
