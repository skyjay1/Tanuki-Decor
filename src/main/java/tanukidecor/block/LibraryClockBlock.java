/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tanukidecor.TDRegistry;
import tanukidecor.block.entity.ClockBlockEntity;
import tanukidecor.util.MultiblockHandler;
import tanukidecor.util.TDBlockShapes;

import java.util.function.Supplier;

public class LibraryClockBlock extends HorizontalMultiblock implements EntityBlock, IChimeProvider {

    protected final Supplier<SoundEvent> chimeSound;
    protected final Supplier<SoundEvent> tickSound;

    public LibraryClockBlock(Supplier<SoundEvent> tickSound, Supplier<SoundEvent> chimeSound, Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_2X3X1, createHorizontalShapeBuilder(MultiblockHandler.MULTIBLOCK_2X3X1, TDBlockShapes.LIBRARY_CLOCK_SHAPE), pProperties);
        this.tickSound = tickSound;
        this.chimeSound = chimeSound;
    }

    //// CHIME PROVIDER ////

    @Nullable
    @Override
    public SoundEvent getChimeSound() {
        return this.chimeSound.get();
    }

    @Nullable
    @Override
    public SoundEvent getTickSound() {
        return this.tickSound.get();
    }

    @Override
    public int getTickSoundInterval() {
        return 40;
    }

    //// BLOCK ENTITY ////


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(this.getMultiblockHandler().isCenterState(pState)) {
            return TDRegistry.BlockEntityReg.LIBRARY_CLOCK_BLOCK_ENTITY.get().create(pPos, pState);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide() ? (BlockEntityTicker<T>) (BlockEntityTicker<ClockBlockEntity>) (ClockBlockEntity::tick) : null;
    }
}
