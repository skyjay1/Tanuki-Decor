/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.blockentity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.joml.Matrix4f;
import tanukidecor.block.entity.DisplayCaseBlockEntity;

public class DisplayCaseBER implements BlockEntityRenderer<DisplayCaseBlockEntity> {

    protected final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    protected final ItemRenderer itemRenderer;
    protected final EntityRenderDispatcher entityRenderDispatcher;
    protected final Font font;

    public DisplayCaseBER(BlockEntityRendererProvider.Context pContext) {
        this.blockEntityRenderDispatcher = pContext.getBlockEntityRenderDispatcher();
        this.entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.font = Minecraft.getInstance().font;
    }

    @Override
    public void render(DisplayCaseBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        // load values
        final BlockState blockState = pBlockEntity.getBlockState();
        final Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        final ItemStack itemStack = pBlockEntity.getItem(0);
        // validate item stack
        if(itemStack.isEmpty()) {
            return;
        }
        // prepare to render
        final Minecraft mc = Minecraft.getInstance();
        final BakedModel itemModel = this.itemRenderer.getModel(itemStack, pBlockEntity.getLevel(), null, 0);

        double dx = 0.5D;
        double dy = 0.0D;
        double dz = 0.5D;

        // start rendering
        pPoseStack.pushPose();

        final float yRot = (direction.getOpposite().toYRot()) * Mth.DEG_TO_RAD;
        pPoseStack.translate(dx, dy, dz);
        pPoseStack.mulPose(Axis.YN.rotation(yRot));
        pPoseStack.translate(-dx, -dy, -dz);
        pPoseStack.translate(0.5D, 0.5D, 0.5D);

        // render item
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, pBlockEntity.getLevel(), 0);

        // finish rendering
        pPoseStack.popPose();

        // render nametag
        if(pBlockEntity.hasCustomName() && shouldRenderNameTag(pBlockEntity)) {
            renderNameTag(pBlockEntity, pBlockEntity.getCustomName(), pPoseStack, pBufferSource, pPackedLight);
        }
    }

    protected boolean shouldRenderNameTag(final BlockEntity blockEntity) {
        final Minecraft mc = Minecraft.getInstance();
        final Vec3 pos = Vec3.atCenterOf(blockEntity.getBlockPos());
        final double distance = mc.player.getBlockReach();
        if(this.entityRenderDispatcher.distanceToSqr(pos.x, pos.y, pos.z) < (distance * distance)
                && mc.hitResult != null
                && mc.hitResult.getType() == HitResult.Type.BLOCK) {
            Vec3 hitLocation = mc.hitResult.getLocation();
            BlockPos blockPos = new BlockPos((int) hitLocation.x(), (int) hitLocation.y(), (int) hitLocation.z());
            BlockState blockState = blockEntity.getLevel().getBlockState(blockPos);
            return blockPos.equals(blockEntity.getBlockPos()) && blockState.is(blockEntity.getBlockState().getBlock());
        }
        return false;
    }

    protected void renderNameTag(BlockEntity blockEntity, Component text, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        float height = (float) blockEntity.getBlockState().getShape(blockEntity.getLevel(), blockEntity.getBlockPos(), CollisionContext.empty()).max(Direction.Axis.Y);

        poseStack.pushPose();

        poseStack.translate(0.0F, 0.5F + height, 0.0F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = poseStack.last().pose();

        float opacityFactor = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int opacity = (int)(opacityFactor * 255.0F) << 24;
        float x = (float)(-font.width(text) / 2);

        font.drawInBatch(text, x, 0.0F, 553648127, false, matrix4f, multiBufferSource, Font.DisplayMode.SEE_THROUGH, opacity, packedLight);
        font.drawInBatch(text, x, 0.0F, -1, false, matrix4f, multiBufferSource, Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }

}
