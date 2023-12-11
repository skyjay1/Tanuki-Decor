/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.clock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.block.HorizontalBlock;
import tanukidecor.block.entity.ClockBlockEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ClockBlock extends HorizontalBlock implements EntityBlock, IChimeProvider {

    public static final Supplier<SoundEvent> NO_SOUND = () -> null;

    protected final Supplier<SoundEvent> tickSound;
    protected final Supplier<SoundEvent> chimeSound;
    protected final Supplier<BlockEntityType<ClockBlockEntity>> blockEntitySupplier;

    /**
     * Simple constructor for a clock that takes up one block
     * @param tickSound the tick sound supplier, use {@link ClockBlock#NO_SOUND} to skip
     * @param chimeSound the chime sound supplier, use {@link ClockBlock#NO_SOUND} to skip
     * @param shape the clock shape in the default direction
     * @param blockEntity the block entity type supplier
     * @param pProperties the block properties
     */
    public ClockBlock(@Nonnull Supplier<SoundEvent> tickSound, @Nonnull Supplier<SoundEvent> chimeSound,
                      VoxelShape shape, @Nonnull Supplier<BlockEntityType<ClockBlockEntity>> blockEntity,
                      Properties pProperties) {
        super(pProperties, HorizontalBlock.createShapeBuilder(shape));
        this.tickSound = tickSound;
        this.chimeSound = chimeSound;
        this.blockEntitySupplier = blockEntity;
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getTickSound() {
        return this.tickSound.get();
    }

    @Nullable
    @Override
    public SoundEvent getChimeSound() {
        return this.chimeSound.get();
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntitySupplier.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }

    //// HELPER METHODS ////

    /**
     * @param context the block place context
     * @return the {@link BlockState} of the block only if it was placed against a horizontal face
     */
    @Nullable
    public BlockState getStateForWallPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluidstate.getType() == Fluids.WATER;
        if (context.getClickedFace().getAxis() != Direction.Axis.Y) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getClickedFace())
                    .setValue(WATERLOGGED, waterlogged);
        } else {
            return null;
        }
    }

    /**
     * @param state the block state
     * @param level the level
     * @param pos the block position
     * @return true if the block behind this one has a solid face
     * @see BlockState#isFaceSturdy(BlockGetter, BlockPos, Direction)
     */
    public boolean canSurviveOnWall(BlockState state, LevelReader level, BlockPos pos) {
        final Direction facing = state.getValue(FACING);
        final BlockPos supportingPos = pos.relative(facing.getOpposite());
        final BlockState supportingState = level.getBlockState(supportingPos);
        return supportingState.isFaceSturdy(level, supportingPos, facing);
    }
}
