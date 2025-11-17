package tanukidecor.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerAccessor {
    
    @Accessor("shoulderEntityLeft")
    void tanukidecor$setShoulderEntityLeft(CompoundTag tag);
    
    @Accessor("shoulderEntityRight")
    void tanukidecor$setShoulderEntityRight(CompoundTag tag);
}
