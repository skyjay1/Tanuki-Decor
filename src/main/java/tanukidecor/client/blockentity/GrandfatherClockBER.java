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

public class GrandfatherClockBER implements BlockEntityRenderer<ClockBlockEntity> {

    public static final ResourceLocation LONG_HAND = new ResourceLocation(TanukiDecor.MODID, "block/grandfather_clock/long_hand");
    public static final ResourceLocation SHORT_HAND = new ResourceLocation(TanukiDecor.MODID, "block/grandfather_clock/short_hand");
    public static final ResourceLocation PENDULUM = new ResourceLocation(TanukiDecor.MODID, "block/grandfather_clock/pendulum");

    private static final Vec3 ROOT_POSITION = new Vec3(0, 3.0F / 16.0F, 0);
    private static final Vec3 ROOT_PIVOT_POINT = new Vec3(8.0D / 16.0D, 0, 8.0D / 16.0D);
    private static final Vec3 HANDS_POSITION = new Vec3(0, 13.0D / 16.0D, 0.98D / 16.0D);
    private static final Vec3 HANDS_PIVOT_POINT = new Vec3(8.0D / 16.0D, 7.0D / 16.0D, 0);
    private static final Vec3 PENDULUM_POSITION = new Vec3(0, -2.5D / 16.0D, 0);
    private static final Vec3 PENDULUM_PIVOT_POINT = new Vec3(8.0D / 16.0D, 18.0D / 16.0D, 0);

    protected final BlockRenderDispatcher blockRenderer;
    protected final ClockRenderHelper clockRenderHelper;

    public GrandfatherClockBER(BlockEntityRendererProvider.Context pContext) {
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
        final float maxPendulumAngle = (float) Math.toRadians(8.5F);
        final float hourRotation = ClockBlockEntity.getHour(dayTime, pPartialTick) * hourAngleInterval;
        final float minuteRotation = ClockBlockEntity.getMinute(dayTime, 0.98F) * Mth.TWO_PI;
        final float pendulumRotation = Mth.sin(ClockBlockEntity.getSecond(level.getGameTime(), pPartialTick) * Mth.PI) * maxPendulumAngle;

        final BakedModel shortHand = mc.getModelManager().getModel(SHORT_HAND);
        final BakedModel longHand = mc.getModelManager().getModel(LONG_HAND);
        final BakedModel pendulum = mc.getModelManager().getModel(PENDULUM);

        // prepare render helper
        this.clockRenderHelper
                .withPoseStack(pPoseStack)
                .withBlockState(blockState)
                .withRenderType(pBufferSource, RenderType.translucentMovingBlock())
                .withPackedLight(pPackedLight)
                .withPackedOverlay(pPackedOverlay)
                .withRotationZ(0);

        // rotate pose stack
        this.clockRenderHelper
                .withPosition(ROOT_POSITION)
                .withPivotPoint(ROOT_PIVOT_POINT)
                .rotateForDirection(direction);

        // render short hand
        this.clockRenderHelper
                .withModel(shortHand)
                .withPosition(HANDS_POSITION)
                .withPivotPoint(HANDS_PIVOT_POINT)
                .withRotationZ(hourRotation)
                .render(blockRenderer);

        // render long hand
        this.clockRenderHelper
                .withModel(longHand)
                .withRotationZ(minuteRotation)
                .render(blockRenderer);

        // render pendulum
        this.clockRenderHelper
                .withModel(pendulum)
                .withPosition(PENDULUM_POSITION)
                .withPivotPoint(PENDULUM_PIVOT_POINT)
                .withRotationZ(pendulumRotation)
                .render(blockRenderer);

        // finish rendering
        pPoseStack.popPose();
    }

    public static void addSpecialModels(final List<ResourceLocation> list) {
        list.add(LONG_HAND);
        list.add(SHORT_HAND);
        list.add(PENDULUM);
    }

}
