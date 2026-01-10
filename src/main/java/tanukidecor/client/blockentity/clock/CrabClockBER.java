/*
 * Copyright (c) 2026 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.blockentity.clock;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import tanukidecor.TanukiDecor;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.Set;

public class CrabClockBER extends ClockBER {

    public static final ResourceLocation LONG_HAND = ResourceLocation.fromNamespaceAndPath(TanukiDecor.MODID, "block/crab_clock/long_hand");
    public static final ResourceLocation SHORT_HAND = ResourceLocation.fromNamespaceAndPath(TanukiDecor.MODID, "block/crab_clock/short_hand");

    public CrabClockBER(BlockEntityRendererProvider.Context pContext) {
        super(pContext, SHORT_HAND, LONG_HAND,
                ROOT_POSITION,
                ROOT_PIVOT_POINT,
                HANDS_POSITION,
                new Vec3(8.0D / 16.0D, 5.0D / 16.0D, 0));
    }

    @Override
    public void render(ClockBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        // prepare to render
        final Minecraft mc = Minecraft.getInstance();
        final BlockState blockState = pBlockEntity.getBlockState();
        final Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        final Level level = pBlockEntity.getLevel();
        // query the day time with an offset to ensure 18000 is noon (etc)
        final long dayTime = (level.getDayTime() + 6000L) % 24000L;
        final float hourAngleInterval = Mth.TWO_PI / 12.0F;
        final float hourRotation = ClockBlockEntity.getHour(dayTime, pPartialTick) * hourAngleInterval;
        final float minuteRotation = ClockBlockEntity.getMinute(dayTime, 0.98F) * Mth.TWO_PI;

        // start rendering
        pPoseStack.pushPose();

        // prepare render helper
        this.clockRenderHelper
                .withPoseStack(pPoseStack)
                .withBlockState(blockState)
                .withRenderType(pBufferSource, RenderType.cutout())
                .withPackedLight(pPackedLight)
                .withPackedOverlay(pPackedOverlay)
                .withPartialTick(pPartialTick)
                .withRotationZ(0)
                .withRandom(level.getRandom());

        // rotate pose stack
        this.clockRenderHelper
                .withPosition(this.rootPosition)
                .withPivotPoint(this.rootPivotPoint)
                .rotateForDirection(direction);

        // render short hand
        if (this.shortHand != null) {
            this.clockRenderHelper
                    .withModel(mc.getModelManager().getModel(this.shortHand))
                    .withPosition(this.handsPosition)
                    .withPivotPoint(this.handsPivotPoint)
                    .withRotationZ(hourRotation)
                    .render(blockRenderer);
        }

        // render long hand
        if (this.longHand != null) {
            this.clockRenderHelper
                    .withModel(mc.getModelManager().getModel(this.longHand))
                    .withPosition(this.handsPosition)
                    .withPivotPoint(new Vec3(8.0D / 16.0D, 5.0D / 16.0D, 0))
                    .withRotationZ(minuteRotation)
                    .render(blockRenderer);
        }
        renderAdditional(this.clockRenderHelper, pBlockEntity, pBufferSource);

        // finish rendering
        pPoseStack.popPose();
    }

    public static void addSpecialModels(final Set<ResourceLocation> list) {
        list.add(LONG_HAND);
        list.add(SHORT_HAND);
    }
}
