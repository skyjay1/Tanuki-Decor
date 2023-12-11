/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.bed;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.block.HorizontalMultiblock;
import tanukidecor.util.MultiblockHandler;

import javax.annotation.Nullable;


public class DoubleBedBlock extends HorizontalMultiblock implements IBedProvider {

    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    public DoubleBedBlock(final VoxelShape[][][] shape, Properties pProperties) {
        super(MultiblockHandler.MULTIBLOCK_2X1X2, HorizontalMultiblock.createHorizontalShapeBuilder(MultiblockHandler.MULTIBLOCK_2X1X2, shape), pProperties);
        this.registerDefaultState(this.multiblockHandler.getCenterState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(OCCUPIED, false)));
    }

    @Override
    protected void createMultiblockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createMultiblockStateDefinition(pBuilder.add(OCCUPIED));
    }

    //// BED ////

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return useBed(pState, pLevel, pPos, pPlayer);
    }

    @Override
    public void removeBed(Level level, BlockPos blockPos, BlockState blockState) {
        this.removeAll(level, getMultiblockHandler().getCenterPos(blockPos, blockState, blockState.getValue(FACING)));
    }

    @Override
    public BlockPos getHeadPos(BlockState blockState, Level level, BlockPos pos) {
        final Direction direction = blockState.getValue(FACING);
        return pos.relative(direction.getOpposite(), 2 - blockState.getValue(getMultiblockHandler().getDepthProperty()));
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter level, BlockPos pos, @Nullable Entity player) {
        return true;
    }

    @Override
    public Direction getBedDirection(BlockState state, LevelReader level, BlockPos pos) {
        return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
    }

    //// SHAPE ////

    /**
     * Shape data for each block in the default horizontal direction, ordered by index {@code [height][width][depth]}
     **/
    public static final VoxelShape[][][] SHAPE = new VoxelShape[][][] {
            // height = 0
            {
                    // width = 0
                    {},
                    // width = 1
                    {
                        Shapes.empty(),
                        Shapes.or(box(0, 2, 3, 16, 8, 16),
                                box(12, 0, 0, 16, 12, 3),
                                box(0, 2, 1, 12, 12, 3),
                                box(0, 12, 1, 6, 16, 3)),
                        Shapes.or(box(0, 2, 0, 16, 8, 13),
                                box(12, 0, 13, 16, 16, 16),
                                box(0, 2, 13, 12, 16, 15),
                                box(0, 16, 13, 6, 20, 15),
                                box(3, 8, 6, 13, 10, 12))
                    },
                    // width = 2
                    {
                        Shapes.empty(),
                        Shapes.or(box(0, 2, 3, 16, 8, 16),
                                box(0, 0, 0, 4, 12, 3),
                                box(4, 2, 1, 16, 12, 3),
                                box(10, 12, 1, 16, 16, 3)),
                        Shapes.or(box(0, 2, 0, 16, 8, 13),
                                box(0, 0, 13, 4, 16, 16),
                                box(4, 2, 13, 16, 16, 15),
                                box(10, 16, 13, 16, 20, 15),
                                box(3, 8, 6, 13, 10, 12))
                    }
            },
            // height = 1
            {},
            // height = 2
            {}
    };
}
