/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.data.EmptyModelData;
import tanukidecor.TanukiDecor;
import tanukidecor.block.entity.ClockBlockEntity;

public class LibraryClockBlockEntityRenderer implements BlockEntityRenderer<ClockBlockEntity> {

    public static final ResourceLocation LONG_HAND = new ResourceLocation(TanukiDecor.MODID, "block/library_clock/long_hand");
    public static final ResourceLocation SHORT_HAND = new ResourceLocation(TanukiDecor.MODID, "block/library_clock/short_hand");
    public static final ResourceLocation PENDULUM = new ResourceLocation(TanukiDecor.MODID, "block/library_clock/pendulum");

    public LibraryClockBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {

    }

    @Override
    public void render(ClockBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        // prepare to render
        final Minecraft mc = Minecraft.getInstance();
        final BlockState blockState = pBlockEntity.getBlockState();
        final BlockPos blockPos = pBlockEntity.getBlockPos();
        final long dayTime = pBlockEntity.getLevel().getDayTime();
        final float maxPendulumAngle = (float) Math.toRadians(20);
        final float hourRotation = pBlockEntity.getHour(dayTime, pPartialTick) * Mth.TWO_PI;
        final float minuteRotation = pBlockEntity.getMinute(dayTime, pPartialTick) * Mth.TWO_PI;
        final float pendulumRotation = Mth.sin(ClockBlockEntity.getSecond(dayTime, pPartialTick) * 8.0F) * maxPendulumAngle;

        final BakedModel shortHand = Minecraft.getInstance().getModelManager().getModel(SHORT_HAND);
        final BakedModel longHand = Minecraft.getInstance().getModelManager().getModel(LONG_HAND);
        final BakedModel pendulum = Minecraft.getInstance().getModelManager().getModel(PENDULUM);

        final RenderType rendertype = RenderType.translucentMovingBlock();// ItemBlockRenderTypes.getMovingBlockRenderType(blockState);
        final VertexConsumer vertexConsumer = pBufferSource.getBuffer(rendertype);
        Quaternion rotation = Vector3f.YP.rotationDegrees(/* TODO */0);

        // prepare to render short hand
        // TODO
        // prepare to render long hand
        // TODO
        // prepare to render pendulum
        pPoseStack.pushPose();
        double dx = 8.0D / 16.0D;
        double dy = 18.0D / 16.0D;
        double dz = -0;
        pPoseStack.translate(-8.0F / 16.0F, -2.5D / 16.0D, 0);
        pPoseStack.translate(dx, dy, dz); // TODO
        rotation = Vector3f.YP.rotationDegrees(/* TODO */0);
        rotation.mul(Vector3f.ZP.rotation(pendulumRotation));
        pPoseStack.mulPose(rotation);
        pPoseStack.translate(-dx, -dy, 0);
        mc.getBlockRenderer().getModelRenderer().renderModel(pPoseStack.last(), vertexConsumer, blockState, pendulum,
                1.0F, 1.0F, 1.0F, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        pPoseStack.popPose();
    }

    public static void addSpecialModels() {
        ForgeModelBakery.addSpecialModel(LONG_HAND);
        ForgeModelBakery.addSpecialModel(SHORT_HAND);
        ForgeModelBakery.addSpecialModel(PENDULUM);
    }
}
