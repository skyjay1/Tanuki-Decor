/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.HorizontalDoubleBlock;
import tanukidecor.block.entity.StorageBlockEntity;

public class GreenWardrobeBlock extends HorizontalDoubleBlock implements EntityBlock {

    public static final VoxelShape UPPER_SHAPE = Shapes.or(
            box(0, 0, 1, 16, 14, 15),
            box(0, 14, 0, 16, 16, 16));
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            box(0, 2, 1, 16, 16, 15),
            box(0, 0, 1, 4, 2, 15),
            box(12, 0, 1, 16, 2, 15));

    public GreenWardrobeBlock(Properties pProperties) {
        super(pProperties, HorizontalDoubleBlock.createShapeBuilder(UPPER_SHAPE, LOWER_SHAPE));
    }

    //// CONTAINER ////

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return StorageBlockEntity.use(pState, pLevel, pPos, pPlayer, pHand, pHit, SoundEvents.BARREL_OPEN);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            StorageBlockEntity.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(pPos.equals(getDelegatePos(pState, pPos))) {
            return TDRegistry.BlockEntityReg.GREEN_WARDROBE.get().create(pPos, pState);
        }
        return TDRegistry.BlockEntityReg.STORAGE_DELEGATE.get().create(pPos, pState);
    }

    //// REDSTONE ////

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }
}