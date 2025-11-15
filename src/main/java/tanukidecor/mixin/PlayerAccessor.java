/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.mixin;

import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.world.entity.player.Player.class)
public interface PlayerAccessor {
    
    @Invoker("setShoulderEntityLeft")
    void tanukidecor$setShoulderEntityLeft(CompoundTag tag);
    
    @Invoker("setShoulderEntityRight")
    void tanukidecor$setShoulderEntityRight(CompoundTag tag);
}
