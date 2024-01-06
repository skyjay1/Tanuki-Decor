/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.blockentity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import tanukidecor.block.entity.DisplayBlockEntity;

public class DisplayCaseBER implements BlockEntityRenderer<DisplayBlockEntity> {

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
    public void render(DisplayBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        // load values
        final Level level = pBlockEntity.getLevel();
        final BlockState blockState = pBlockEntity.getBlockState();
        final BlockPos blockPos = pBlockEntity.getBlockPos();
        final ItemStack itemStack = pBlockEntity.getItem(0);
        // validate item stack
        if(itemStack.isEmpty()) {
            return;
        }

        double dx = 0.5D;
        double dy = 0.5D;
        double dz = 0.5D;
        final Vector3f translation = new Vector3f(0.5F, 0.5F, 0.5F);
        translation.add(pBlockEntity.getDisplayTranslation(level, blockState, blockPos, itemStack, pPartialTick));
        final Vector3f rotation = pBlockEntity.getDisplayRotation(level, blockState, blockPos, itemStack, pPartialTick);
        final Vector3f scale = pBlockEntity.getDisplayScale(level, blockState, blockPos, itemStack, pPartialTick);

        Quaternion quaternion = Vector3f.XP.rotationDegrees(rotation.x());
        quaternion.mul(Vector3f.YP.rotationDegrees(rotation.y()));
        quaternion.mul(Vector3f.ZP.rotationDegrees(rotation.z()));

        // start rendering
        pPoseStack.pushPose();

        pPoseStack.translate(dx, dy, dz);
        pPoseStack.mulPose(quaternion);
        pPoseStack.translate(-dx, -dy, -dz);
        pPoseStack.translate(translation.x(), translation.y(), translation.z());
        pPoseStack.scale(scale.x(), scale.y(), scale.z());

        // render item
        this.itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, 0);

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
        final double distance = mc.player.getReachDistance();
        if(this.entityRenderDispatcher.distanceToSqr(pos.x, pos.y, pos.z) < (distance * distance)
                && mc.hitResult != null
                && mc.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = new BlockPos(mc.hitResult.getLocation());
            BlockState blockState = blockEntity.getLevel().getBlockState(blockPos);
            return blockPos.equals(blockEntity.getBlockPos()) && blockState.is(blockEntity.getBlockState().getBlock());
        }
        return false;
    }

    protected void renderNameTag(BlockEntity blockEntity, Component text, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        float height = (float) blockEntity.getBlockState().getShape(blockEntity.getLevel(), blockEntity.getBlockPos(), CollisionContext.empty()).max(Direction.Axis.Y);
        poseStack.pushPose();

        poseStack.translate(0.5D, 0.5D + height, 0.5D);
        poseStack.mulPose(this.blockEntityRenderDispatcher.camera.rotation());
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = poseStack.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int)(f1 * 255.0F) << 24;
        float f2 = (float)(-this.font.width(text) / 2);
        this.font.drawInBatch(text, f2, 0.0F, 553648127, false, matrix4f, multiBufferSource, true, j, packedLight);
        this.font.drawInBatch(text, f2, 0.0F, -1, false, matrix4f, multiBufferSource, false, 0, packedLight);

        poseStack.popPose();
    }

}
