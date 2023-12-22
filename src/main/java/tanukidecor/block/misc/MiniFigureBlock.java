/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import tanukidecor.TDRegistry;
import tanukidecor.block.RotatingBlock;

public class MiniFigureBlock extends RotatingBlock {

    public static final VoxelShape SHAPE = box(4, 0, 4, 12, 12, 12);

    public MiniFigureBlock(Properties pProperties) {
        super(pProperties, b -> SHAPE);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pPlayer.isShiftKeyDown()) {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        // play sound
        pLevel.playSound(pPlayer, pPos, TDRegistry.SoundReg.MINI_FIGURE_SQUEAK.get(), SoundSource.BLOCKS, 1.0F, 0.8F + pPlayer.getRandom().nextFloat() * 0.4F);
        return InteractionResult.SUCCESS;
    }
}
