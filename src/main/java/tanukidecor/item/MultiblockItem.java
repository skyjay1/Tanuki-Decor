/**
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 **/

package tanukidecor.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import tanukidecor.block.HorizontalMultiblock;
import tanukidecor.util.MultiblockHandler;

import javax.annotation.Nullable;

public class MultiblockItem extends BlockItem {

    public MultiblockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext pContext) {
        final BlockPos blockpos = pContext.getClickedPos();
        final Direction direction = pContext.getHorizontalDirection();
        final MultiblockHandler multiblockHandler = ((HorizontalMultiblock) this.getBlock()).getMultiblockHandler();
        final Vec3i dimensions = multiblockHandler.getDimensions();
        // determine the center position of a multiblock placed with the given position and rotation
        final Vec3i index = Vec3i.ZERO; // TODO fix math new Vec3i(-(direction.getStepX() - (dimensions.getX() / 2)), 0, -(direction.getStepZ() - (dimensions.getZ() / 2)));
        final BlockPos center = multiblockHandler.getCenter(blockpos, index);
        // create a block place context at the center position
        return BlockPlaceContext.at(pContext, center, direction);
    }

    @Override
    protected boolean mustSurvive() {
        return false;
    }
}
