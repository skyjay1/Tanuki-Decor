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

public class AlarmClockBlock extends HorizontalBlock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> tickSound;
    protected final Supplier<SoundEvent> chimeSound;

    public static final VoxelShape SHAPE = Shapes.or(
            box(3, 2, 5, 13, 12, 11),
            box(6.5D, 12, 8, 9.5D, 15, 8),
            box(2, 0, 7, 4, 4, 9),
            box(12, 0, 7, 14, 4, 9));

    public AlarmClockBlock(Supplier<SoundEvent> tickSound, Supplier<SoundEvent> chimeSound, Properties pProperties) {
        super(pProperties, HorizontalBlock.createShapeBuilder(SHAPE));
        this.tickSound = tickSound;
        this.chimeSound = chimeSound;
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

    @Override
    public boolean isTimeToChime(long dayTime) {
        return dayTime == DAWN;
    }

    //// BLOCK ENTITY ////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return TDRegistry.BlockEntityReg.ALARM_CLOCK.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }
}
