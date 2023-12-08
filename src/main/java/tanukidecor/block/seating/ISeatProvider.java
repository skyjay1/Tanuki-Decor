/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.seating;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public interface ISeatProvider {

    public static final Predicate<Entity> IS_SEAT_ENTITY = e ->
            e.getType() == EntityType.PIG && e.isSilent() && e.isInvulnerable() && e.isNoGravity();

    /**
     * @return the vertical offset of the seat entity, typically 2 pixels above the seating part of the model
     */
    double getSeatYOffset();

    /**
     * @param blockState the block state
     * @param level the level
     * @param blockPos the block position
     * @return the position of the seat entity
     */
    default Vec3 getSeatPosition(BlockState blockState, Level level, BlockPos blockPos) {
        return new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + getSeatYOffset(), blockPos.getZ() + 0.5D);
    }

    /**
     * @param blockState the block state
     * @param level the level
     * @param pos the block position
     * @param force true to despawn the seat even if it has a passenger
     * @return true if the seat entity had no passengers and was removed
     */
    default boolean despawnSeat(BlockState blockState, Level level, BlockPos pos, boolean force) {
        Entity entity = getSeat(level, pos);
        if(entity != null && (force || !entity.isVehicle())) {
            entity.ejectPassengers();
            entity.discard();
            return true;
        }
        return false;
    }

    /**
     * @param blockState the block state
     * @param level the level
     * @param pos the block position
     * @param player the player
     * @return true if the seat entity exists and the player was added as a passenger
     */
    default boolean startSitting(BlockState blockState, Level level, BlockPos pos, Player player) {
        Entity entity = getOrCreateSeat(blockState, level, pos);
        // verify entity exists and can be vehicle
        if(null == entity || entity.isVehicle()) {
            return false;
        }
        // add passenger
        return player.startRiding(entity, true);
    }

    /**
     * @param blockState the block state
     * @param level the level
     * @param pos the block position
     * @return true if the seat entity exists and at the passenger was ejected
     */
    default boolean stopSitting(BlockState blockState, Level level, BlockPos pos) {
        // locate existing entity, if any
        Entity entity = getSeat(level, pos);
        if(null == entity || !entity.isVehicle()) {
            return false;
        }
        entity.ejectPassengers();
        return true;
    }

    /**
     * @param level the level
     * @param pos the block position
     * @return the entity to use as the seat
     * @see #getSeat(Level, BlockPos)
     */
    default @Nullable Entity getOrCreateSeat(BlockState blockState, Level level, BlockPos pos) {
        // locate existing entity, if an y
        final Entity existingEntity = getSeat(level, pos);
        if(existingEntity != null) {
            return existingEntity;
        }
        // otherwise, create new entity
        final Vec3 seatPos = getSeatPosition(blockState, level, pos);
        final Pig entity = EntityType.PIG.create(level);
        if(entity != null) {
            entity.setPos(seatPos.add(0, -entity.getPassengersRidingOffset(), 0));
            // entity settings
            entity.setNoAi(true);
            entity.setSilent(true);
            entity.setNoGravity(true);
            entity.setInvisible(true);
            entity.setInvulnerable(true);
            entity.setBaby(true);
            entity.noPhysics = true;
            entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
            // add entity to level
            level.addFreshEntity(entity);
        }
        return entity;
    }

    /**
     * @param level the level
     * @param pos the block position
     * @return the entity to use as the seat, if one exists
     */
    default @Nullable Entity getSeat(Level level, BlockPos pos) {
        final AABB aabb = new AABB(pos).inflate(0, 0.25D, 0);
        final List<Entity> list = level.getEntitiesOfClass(Entity.class, aabb, IS_SEAT_ENTITY);
        if(!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
