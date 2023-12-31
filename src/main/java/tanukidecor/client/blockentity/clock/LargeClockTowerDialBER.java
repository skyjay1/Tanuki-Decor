/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.client.blockentity.clock;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import tanukidecor.TanukiDecor;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.Set;

public class LargeClockTowerDialBER extends ClockBER {

    public static final ResourceLocation LONG_HAND = new ResourceLocation(TanukiDecor.MODID, "block/large_clock_tower_dial/long_hand");
    public static final ResourceLocation SHORT_HAND = new ResourceLocation(TanukiDecor.MODID, "block/large_clock_tower_dial/short_hand");

    public LargeClockTowerDialBER(BlockEntityRendererProvider.Context pContext) {
        super(pContext, SHORT_HAND, LONG_HAND,
                ROOT_POSITION,
                ROOT_PIVOT_POINT,
                new Vec3(-8.0D / 16.0D, -8.0D / 16.0D, 0),
                new Vec3(8.0D / 16.0D, 8.0D / 16.0D, 0));
    }

    @Override
    public void render(ClockBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
       super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
    }

    public static void addSpecialModels(final Set<ResourceLocation> list) {
        list.add(LONG_HAND);
        list.add(SHORT_HAND);
    }
}
