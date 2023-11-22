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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import tanukidecor.TanukiDecor;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.List;

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
        final Direction direction = blockState.getValue(HorizontalDirectionalBlock.FACING);
        final Level level = pBlockEntity.getLevel();
        // query the day time with an offset to ensure 18000 is noon (etc)
        final long dayTime = (level.getDayTime() + 6000L) % 24000L;
        final float hourAngleInterval = Mth.TWO_PI / 12.0F;
        final float maxPendulumAngle = (float) Math.toRadians(16);
        final float hourRotation = ClockBlockEntity.getHour(dayTime, pPartialTick) * hourAngleInterval;
        final float minuteRotation = ClockBlockEntity.getMinute(dayTime, 0.98F) * Mth.TWO_PI;
        final float pendulumRotation = Mth.sin(ClockBlockEntity.getSecond(level.getGameTime(), pPartialTick) * Mth.PI) * maxPendulumAngle;

        final BakedModel shortHand = mc.getModelManager().getModel(SHORT_HAND);
        final BakedModel longHand = mc.getModelManager().getModel(LONG_HAND);
        final BakedModel pendulum = mc.getModelManager().getModel(PENDULUM);

        final RenderType rendertype = RenderType.translucentMovingBlock();
        final VertexConsumer vertexConsumer = pBufferSource.getBuffer(rendertype);
        final float yRot = (direction.getOpposite().toYRot()) * Mth.DEG_TO_RAD;
        double dx = 8.0D / 16.0D;
        double dy = 0;
        double dz = 8.0D / 16.0D;

        // prepare to render all
        pPoseStack.pushPose();
        pPoseStack.translate(dx, dy, dz);
        pPoseStack.mulPose(Vector3f.YN.rotation(yRot));
        pPoseStack.translate(-dx, -dy, -dz);
        pPoseStack.translate(-8.0D / 16.0D, 0, 0D / 16.0D);

        // prepare to render short hand
        pPoseStack.pushPose();
        pPoseStack.translate(0, 13.0D / 16.0D, 0D);
        dx = 8.0D / 16.0D;
        dy = 11.0D / 16.0D;
        dz = 0;
        pPoseStack.translate(dx, dy, dz);
        pPoseStack.mulPose(Vector3f.ZP.rotation(hourRotation));
        pPoseStack.translate(-dx, -dy, -dz);
        mc.getBlockRenderer().getModelRenderer().renderModel(pPoseStack.last(), vertexConsumer, blockState, shortHand,
                1.0F, 1.0F, 1.0F, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        pPoseStack.popPose();

        // prepare to render long hand
        pPoseStack.pushPose();
        pPoseStack.translate(0, 13.0D / 16.0D, 0D);
        pPoseStack.translate(dx, dy, dz);
        pPoseStack.mulPose(Vector3f.ZP.rotation(minuteRotation));
        pPoseStack.translate(-dx, -dy, -dz);
        mc.getBlockRenderer().getModelRenderer().renderModel(pPoseStack.last(), vertexConsumer, blockState, longHand,
                1.0F, 1.0F, 1.0F, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        pPoseStack.popPose();

        // prepare to render pendulum
        pPoseStack.pushPose();
        pPoseStack.translate(0, -2.5D / 16.0D, 0);
        dx = 8.0D / 16.0D;
        dy = 18.0D / 16.0D;
        dz = 0;
        pPoseStack.translate(dx, dy, dz);
        pPoseStack.mulPose(Vector3f.ZP.rotation(pendulumRotation));
        pPoseStack.translate(-dx, -dy, -dz);
        mc.getBlockRenderer().getModelRenderer().renderModel(pPoseStack.last(), vertexConsumer, blockState, pendulum,
                1.0F, 1.0F, 1.0F, pPackedLight, pPackedOverlay, EmptyModelData.INSTANCE);
        pPoseStack.popPose();

        // finish rendering
        pPoseStack.popPose();
    }

    public static void addSpecialModels(final List<ResourceLocation> list) {
        list.add(LONG_HAND);
        list.add(SHORT_HAND);
        list.add(PENDULUM);
    }
}
