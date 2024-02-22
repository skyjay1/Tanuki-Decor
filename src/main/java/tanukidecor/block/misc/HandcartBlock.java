/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.RotatingWideBlock;
import tanukidecor.block.Side;

public class HandcartBlock extends RotatingWideBlock implements EntityBlock {

    public static final VoxelShape SHAPE_EAST = Shapes.block();
    public static final VoxelShape SHAPE_WEST = Shapes.block();

    public HandcartBlock(Properties pProperties) {
        super(pProperties, RotatingWideBlock.createShapeBuilder(SHAPE_EAST, SHAPE_WEST));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(this.getDelegatePos(pState, pPos).equals(pPos)) {
            return TDRegistry.BlockEntityReg.HANDCART.get().create(pPos, pState);
        }
        return TDRegistry.BlockEntityReg.STORAGE_DELEGATE.get().create(pPos, pState);
    }
}
