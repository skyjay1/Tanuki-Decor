package tanukidecor.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tanukidecor.TanukiDecor;

import java.lang.reflect.Method;

public class ReflectionHelper {
    
    private static Method setShoulderEntityLeftMethod;
    private static Method setShoulderEntityRightMethod;
    private static Method getPassengersRidingOffsetMethod;
    
    static {
        try {
            setShoulderEntityLeftMethod = Player.class.getDeclaredMethod("setShoulderEntityLeft", CompoundTag.class);
            setShoulderEntityLeftMethod.setAccessible(true);
            
            setShoulderEntityRightMethod = Player.class.getDeclaredMethod("setShoulderEntityRight", CompoundTag.class);
            setShoulderEntityRightMethod.setAccessible(true);
            
            getPassengersRidingOffsetMethod = Entity.class.getDeclaredMethod("getPassengersRidingOffset");
            getPassengersRidingOffsetMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            TanukiDecor.LOGGER.error("Failed to find reflected methods", e);
        }
    }
    
    public static void setShoulderEntityLeft(Player player, CompoundTag tag) {
        try {
            setShoulderEntityLeftMethod.invoke(player, tag);
        } catch (Exception e) {
            TanukiDecor.LOGGER.error("Failed to set left shoulder entity", e);
        }
    }
    
    public static void setShoulderEntityRight(Player player, CompoundTag tag) {
        try {
            setShoulderEntityRightMethod.invoke(player, tag);
        } catch (Exception e) {
            TanukiDecor.LOGGER.error("Failed to set right shoulder entity", e);
        }
    }
    
    public static double getPassengersRidingOffset(Entity entity) {
        try {
            return (double) getPassengersRidingOffsetMethod.invoke(entity);
        } catch (Exception e) {
            TanukiDecor.LOGGER.error("Failed to get passengers riding offset", e);
            return 0.6875;
        }
    }
}
