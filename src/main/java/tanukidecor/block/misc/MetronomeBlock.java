/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.RotatingBlock;
import tanukidecor.block.clock.IChimeProvider;
import tanukidecor.block.entity.MetronomeBlockEntity;

import java.util.function.Supplier;

public class MetronomeBlock extends RotatingBlock implements EntityBlock, IChimeProvider {

    public static final int MAX_SPEED = 7;
    public static final IntegerProperty SPEED = IntegerProperty.create("speed", 0, MAX_SPEED);

    protected final Supplier<SoundEvent> tickSound;

    public static final VoxelShape SHAPE = Shapes.or(
            box(4, 0, 5, 12, 3, 11),
            box(5, 3, 7, 11, 10, 11),
            box(7, 10, 7, 9, 11, 11));

    public MetronomeBlock(Properties pProperties) {
        super(pProperties, RotatingBlock.createShapeBuilder(SHAPE));
        this.tickSound = TDRegistry.SoundReg.POCKET_WATCH_TICK;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SPEED, 1)
                .setValue(WATERLOGGED, false));
    }

    //// METHODS ////

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(SPEED));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if(!pPlayer.isShiftKeyDown()) {
            pLevel.setBlock(pPos, pState.cycle(SPEED), Block.UPDATE_ALL);
            pLevel.updateNeighbourForOutputSignal(pPos, pState.getBlock());
            return InteractionResult.SUCCESS;
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getTickSound(BlockState blockState) {
        return blockState.getValue(SPEED) > 0 ? SoundEvents.STONE_BUTTON_CLICK_ON : null;
    }

    @Override
    public int getTickSoundInterval(BlockState blockState) {
        final int speed = blockState.getValue(SPEED);
        if(speed == 0) {
            return Integer.MAX_VALUE;
        }
        return 2 + (MAX_SPEED - speed) * 4;
    }

    @Override
    public boolean isTimeToChime(BlockState blockState, long dayTime) {
        return false;
    }


    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return TDRegistry.BlockEntityReg.METRONOME.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (BlockEntityTicker<T>) (BlockEntityTicker<MetronomeBlockEntity>) (MetronomeBlockEntity::tick);
    }

    //// REDSTONE ////

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(SPEED);
    }
}
