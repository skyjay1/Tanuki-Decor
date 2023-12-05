/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.clock;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.HorizontalBlock;
import tanukidecor.block.entity.ClockBlockEntity;

import java.util.function.Supplier;

public class AnniversaryClockBlock extends HorizontalBlock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> tickSound;

    public static final VoxelShape SHAPE = Shapes.or(
            box(1, 0, 1, 15, 2, 15),
            box(2, 2, 2, 14, 18, 14));

    public AnniversaryClockBlock(Supplier<SoundEvent> tickSound, Properties pProperties) {
        super(pProperties, HorizontalBlock.createShapeBuilder(SHAPE));
        this.tickSound = tickSound;
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getTickSound() {
        return this.tickSound.get();
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return TDRegistry.BlockEntityReg.ANNIVERSARY_CLOCK.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }
}
