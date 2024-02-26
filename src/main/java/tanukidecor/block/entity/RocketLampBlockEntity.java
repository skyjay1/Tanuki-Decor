/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import tanukidecor.block.misc.RocketLampBlock;

public class RocketLampBlockEntity extends BlockEntity {

    public RocketLampBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public String getColor() {
        if(getBlockState().getBlock() instanceof RocketLampBlock block) {
            return block.getColor();
        }
        return "";
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).inflate(0.5F, 1.0F, 0.5F);
    }
}
