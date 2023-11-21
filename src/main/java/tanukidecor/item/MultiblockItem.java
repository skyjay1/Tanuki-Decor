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
import tanukidecor.util.ShapeUtils;

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
        // determine the index of the clicked position and the desired center position
        final Vec3i clickedIndex = new Vec3i(0, -1, 0); // TODO math new Vec3i(-(direction.getStepX() - centerIndex.getX()), 0, -(direction.getStepZ() - centerIndex.getZ()));
        final BlockPos center = MultiblockHandler.getCenterPos(pContext.getClickedPos(), clickedIndex, direction.getOpposite());
        // create a block place context at the center position
        return BlockPlaceContext.at(pContext, center, direction);
    }

    @Override
    protected boolean mustSurvive() {
        return false;
    }
}
