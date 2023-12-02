/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import tanukidecor.TanukiDecor;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.List;

public class ClockBER implements BlockEntityRenderer<ClockBlockEntity> {

    public static final ResourceLocation EMPTY = new ResourceLocation(TanukiDecor.MODID, "block/library_clock/empty");

    protected final BlockRenderDispatcher blockRenderer;
    protected final ClockRenderHelper clockRenderHelper;
    protected final ResourceLocation shortHand;
    protected final ResourceLocation longHand;
    protected final Vec3 rootPosition;
    protected final Vec3 rootPivotPoint;
    protected final Vec3 handsPosition;
    protected final Vec3 handsPivotPoint;

    public ClockBER(BlockEntityRendererProvider.Context pContext,
                    ResourceLocation shortHand, ResourceLocation longHand,
                    Vec3 rootPosition, Vec3 rootPivotPoint,
                    Vec3 handsPosition, Vec3 handsPivotPoint) {
        this.shortHand = shortHand;
        this.longHand = longHand;
        this.rootPosition = rootPosition;
        this.rootPivotPoint = rootPivotPoint;
        this.handsPosition = handsPosition;
        this.handsPivotPoint = handsPivotPoint;
        this.blockRenderer = pContext.getBlockRenderDispatcher();
        this.clockRenderHelper = new ClockRenderHelper();
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
        final float hourRotation = ClockBlockEntity.getHour(dayTime, pPartialTick) * hourAngleInterval;
        final float minuteRotation = ClockBlockEntity.getMinute(dayTime, 0.98F) * Mth.TWO_PI;

        // prepare render helper
        this.clockRenderHelper
                .withPoseStack(pPoseStack)
                .withBlockState(blockState)
                .withRenderType(pBufferSource, RenderType.translucentMovingBlock())
                .withPackedLight(pPackedLight)
                .withPackedOverlay(pPackedOverlay)
                .withPartialTick(pPartialTick)
                .withRotationZ(0);

        // rotate pose stack
        this.clockRenderHelper
                .withPosition(this.rootPosition)
                .withPivotPoint(this.rootPivotPoint)
                .rotateForDirection(direction);

        // render short hand
        if(this.shortHand != null) {
            this.clockRenderHelper
                    .withModel(mc.getModelManager().getModel(this.shortHand))
                    .withPosition(this.handsPosition)
                    .withPivotPoint(this.handsPivotPoint)
                    .withRotationZ(hourRotation)
                    .render(blockRenderer);
        }

        // render long hand
        if(this.longHand != null) {
            this.clockRenderHelper
                    .withModel(mc.getModelManager().getModel(this.longHand))
                    .withPosition(this.handsPosition)
                    .withPivotPoint(this.handsPivotPoint)
                    .withRotationZ(minuteRotation)
                    .render(blockRenderer);
        }
        renderAdditional(this.clockRenderHelper, pBlockEntity, pBufferSource);

        // finish rendering
        pPoseStack.popPose();
    }

    /**
     * Allows implementations to render additional models
     * @param renderHelper the clock render helper
     * @param blockEntity the block entity
     * @param bufferSource the buffer source
     */
    public void renderAdditional(ClockRenderHelper renderHelper, ClockBlockEntity blockEntity,  MultiBufferSource bufferSource) {
        // do nothing
    }
}