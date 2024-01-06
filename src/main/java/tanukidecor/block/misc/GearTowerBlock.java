/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.TallBlock;

public class GearTowerBlock extends TallBlock implements EntityBlock {

    public static final VoxelShape SHAPE_UPPER = Shapes.or(
            box(7, 0, 7, 9, 16, 9),
            box(6, 0, 6, 10, 2, 10),
            box(6, 6, 6, 10, 9, 10),
            box(6, 13, 6, 10, 16, 10));
    public static final VoxelShape SHAPE_LOWER = Shapes.or(
            box(7, 0, 7, 9, 16, 9),
            box(6, 0, 6, 10, 3, 10),
            box(6, 7, 6, 10, 10, 10),
            box(6, 14, 6, 10, 16, 10));

    public GearTowerBlock(Properties pProperties) {
        super(SHAPE_UPPER, SHAPE_LOWER, pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(this.getDelegatePos(pState, pPos).equals(pPos)) {
            return TDRegistry.BlockEntityReg.GEAR_TOWER.get().create(pPos, pState);
        }
        return null;
    }
}
