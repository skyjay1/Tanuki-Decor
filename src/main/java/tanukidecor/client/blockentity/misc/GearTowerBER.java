/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.blockentity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.data.EmptyModelData;
import tanukidecor.TanukiDecor;
import tanukidecor.block.entity.GearTowerBlockEntity;
import tanukidecor.block.misc.PhonographBlock;

import java.util.Set;

public class GearTowerBER implements BlockEntityRenderer<GearTowerBlockEntity> {

    public static final ResourceLocation BIG_SAW_GEAR = new ResourceLocation(TanukiDecor.MODID, "block/gear_tower/big_saw_gear");
    public static final ResourceLocation BIG_SQUARE_GEAR = new ResourceLocation(TanukiDecor.MODID, "block/gear_tower/big_square_gear");
    public static final ResourceLocation SMALL_SAW_GEAR = new ResourceLocation(TanukiDecor.MODID, "block/gear_tower/small_saw_gear");
    public static final ResourceLocation SMALL_SQUARE_GEAR = new ResourceLocation(TanukiDecor.MODID, "block/gear_tower/small_square_gear");

    protected final BlockRenderDispatcher blockRenderer;

    public GearTowerBER(BlockEntityRendererProvider.Context pContext) {
        this.blockRenderer = pContext.getBlockRenderDispatcher();
    }

    @Override
    public void render(GearTowerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        // prepare to render
        final BlockState blockState = pBlockEntity.getBlockState();
        final float time = pBlockEntity.getLevel().getGameTime() + pPartialTick;

        final RenderType renderType = RenderType.cutout();
        final VertexConsumer vertexConsumer = pBufferSource.getBuffer(renderType);

        final Minecraft mc = Minecraft.getInstance();
        final BakedModel bigSawGearModel = mc.getModelManager().getModel(BIG_SAW_GEAR);
        final BakedModel bigSquareGearModel = mc.getModelManager().getModel(BIG_SQUARE_GEAR);
        final BakedModel smallSawGearModel = mc.getModelManager().getModel(SMALL_SAW_GEAR);
        final BakedModel smallSquareGearModel = mc.getModelManager().getModel(SMALL_SQUARE_GEAR);

        // start rendering
        pPoseStack.pushPose();

        // render gears
        double dx = 0.5D;
        double dy = 0.0D;
        double dz = 0.5D;
        float yRot = time * 0.085F;
        pPoseStack.translate(dx, dy, dz);
        pPoseStack.mulPose(Vector3f.YN.rotation(yRot));
        pPoseStack.translate(-dx, -dy, -dz);
        // render lower gears
        pPoseStack.translate(0, -1.0F, 0);
        blockRenderer.getModelRenderer().renderModel(pPoseStack.last(), vertexConsumer, blockState, bigSawGearModel,
                1.0F, 1.0F, 1.0F, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        blockRenderer.getModelRenderer().renderModel(pPoseStack.last(), vertexConsumer, blockState, smallSquareGearModel,
                1.0F, 1.0F, 1.0F, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        // render upper gears
        pPoseStack.translate(0, 1.0F, 0);
        blockRenderer.getModelRenderer().renderModel(pPoseStack.last(), vertexConsumer, blockState, smallSawGearModel,
                1.0F, 1.0F, 1.0F, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        blockRenderer.getModelRenderer().renderModel(pPoseStack.last(), vertexConsumer, blockState, bigSquareGearModel,
                1.0F, 1.0F, 1.0F, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);

        // finish rendering
        pPoseStack.popPose();
    }

    public static void addSpecialModels(final Set<ResourceLocation> set) {
        set.add(BIG_SAW_GEAR);
        set.add(BIG_SQUARE_GEAR);
        set.add(SMALL_SAW_GEAR);
        set.add(SMALL_SQUARE_GEAR);
    }
}