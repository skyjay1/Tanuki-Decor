/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import tanukidecor.TDRegistry;
import tanukidecor.block.RotatingTallBlock;
import tanukidecor.block.misc.PhonographBlock;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PhonographBlockEntity extends SingleSlotBlockEntity {

    protected boolean isPlaying;
    protected long recordStartedTick;
    protected long tickCount;
    protected int ticksSinceLastEvent;

    public PhonographBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    //// METHODS ////

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, PhonographBlockEntity blockEntity) {
        blockEntity.phonographTick(level, blockPos, blockState);
    }

    public static InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // validate block entity
        if(!(level.getBlockEntity(pos) instanceof PhonographBlockEntity blockEntity)) {
            return InteractionResult.FAIL;
        }
        // validate side
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        // drop recording, if any
        if(!blockEntity.isEmpty()) {
            ItemStack record = blockEntity.getFirstItem();
            // remove item
            blockEntity.clearContent();
            // give to player
            if(player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand, record);
            } else if(!player.getInventory().add(record)) {
                Block.popResource(level, pos.above(), record);
            }
            // stop playing sound
            level.levelEvent(LevelEvent.SOUND_STOP_JUKEBOX_SONG, pos, 0);
            blockEntity.setHasRecordBlockState(player, false);
            return InteractionResult.SUCCESS;
        }
        // insert recording, if any
        final ItemStack itemStack = player.getItemInHand(hand);
        if(blockEntity.isEmpty() && !itemStack.isEmpty() && blockEntity.canPlaceItem(0, itemStack)) {
            // update item stack (sound is handled in setChanged)
            ItemStack record = itemStack.split(blockEntity.getMaxStackSize());
            blockEntity.setFirstItem(record);
            blockEntity.setHasRecordBlockState(player, true);
            // award stat to player
            player.awardStat(Stats.PLAY_RECORD);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    public static void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof PhonographBlockEntity blockEntity) {
            // drop contents and stop music
            if(!blockEntity.isEmpty()) {
                level.levelEvent(LevelEvent.SOUND_STOP_JUKEBOX_SONG, pos, 0);
                blockEntity.dropContents();
            }
        }
    }

    protected void phonographTick(Level level, BlockPos blockPos, BlockState blockState) {
        ++this.ticksSinceLastEvent;
        if (this.isRecordPlaying()) {
            Item item = this.getFirstItem().getItem();
            if (item instanceof RecordItem record) {
                if (this.shouldRecordStopPlaying(record)) {
                    this.stopPlaying();
                } else if (this.shouldSendJukeboxPlayingEvent()) {
                    // TODO Allay#shouldStopDancing is hardcoded to check for a jukebox at the given position
                    this.ticksSinceLastEvent = 0;
                    //level.gameEvent(GameEvent.JUKEBOX_PLAY, blockPos, GameEvent.Context.of(blockState));
                    this.spawnMusicParticles(level, blockPos.above());
                }
            }
        }
        ++this.tickCount;
    }

    protected boolean shouldSendJukeboxPlayingEvent() {
        return this.ticksSinceLastEvent >= 20;
    }

    protected void setHasRecordBlockState(@Nullable Entity entity, boolean hasRecord) {
        if (this.level != null && this.level.getBlockState(this.getBlockPos()) == this.getBlockState()) {
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(PhonographBlock.HAS_RECORD, hasRecord), Block.UPDATE_ALL);
            this.level.gameEvent(entity, GameEvent.BLOCK_CHANGE, this.getBlockPos());
            this.level.setBlock(this.getBlockPos().above(), this.level.getBlockState(this.getBlockPos().above()).setValue(PhonographBlock.HAS_RECORD, hasRecord), Block.UPDATE_CLIENTS);
            this.level.gameEvent(entity, GameEvent.BLOCK_CHANGE, this.getBlockPos().above());
        }
    }

    protected void spawnMusicParticles(Level pLevel, BlockPos pPos) {
        if (pLevel instanceof ServerLevel serverlevel) {
            Direction direction = getBlockState().getValue(RotatingTallBlock.FACING);
            Vec3 vec3 = Vec3.atCenterOf(pPos).add(0.4D * direction.getStepX(), -0.125D, 0.4D * direction.getStepZ());
            float f = (float)pLevel.getRandom().nextInt(4) / 24.0F;
            serverlevel.sendParticles(ParticleTypes.NOTE, vec3.x(), vec3.y(), vec3.z(), 0, (double)f, 0.0D, 0.0D, 1.0D);
        }

    }

    //// CONTAINER ////

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return pStack.is(ItemTags.MUSIC_DISCS) && this.getItem(pIndex).isEmpty();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public void setItem(int pSlot, ItemStack pStack) {
        super.setItem(pSlot, pStack);
        if (pStack.is(ItemTags.MUSIC_DISCS) && this.level != null) {
            this.getInventory().set(pSlot, pStack);
            this.setHasRecordBlockState(null, true);
            this.startPlaying();
        }
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getInventory(), pSlot, pAmount);
        if (!itemstack.isEmpty()) {
            this.setHasRecordBlockState(null, false);
            this.stopPlaying();
        }

        return itemstack;
    }

    public ItemStack getFirstItem() {
        return this.getItem(0);
    }

    public void setFirstItem(ItemStack pRecord) {
        this.setItem(0, pRecord);
    }

    //// RECORD ////

    protected void startPlaying() {
        this.recordStartedTick = this.tickCount;
        this.isPlaying = true;
        this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        this.level.levelEvent(null, LevelEvent.SOUND_PLAY_JUKEBOX_SONG, this.getBlockPos(), Item.getId(this.getFirstItem().getItem()));
        this.setChanged();
    }

    protected void stopPlaying() {
        this.isPlaying = false;
        this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        this.level.levelEvent(LevelEvent.SOUND_STOP_JUKEBOX_SONG, this.getBlockPos(), 0);
        this.setChanged();
    }

    protected long getRecordLengthInTicks(final RecordItem recordItem) {
        return recordItem.getLengthInTicks();
    }

    protected boolean shouldRecordStopPlaying(RecordItem recordItem) {
        return this.tickCount >= this.recordStartedTick + getRecordLengthInTicks(recordItem) + 20L;
    }

    public boolean isRecordPlaying() {
        return !this.getFirstItem().isEmpty() && this.isPlaying;
    }

    //// CLEARABLE ////

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    //// NBT ////

    private static final String KEY_IS_PLAYING = "IsPlaying";
    private static final String KEY_RECORD_STARTED_TICK = "RecordStartTick";
    private static final String KEY_TICK_COUNT = "TickCount";

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.isPlaying = pTag.getBoolean(KEY_IS_PLAYING);
        this.recordStartedTick = pTag.getLong(KEY_RECORD_STARTED_TICK);
        this.tickCount = pTag.getLong(KEY_TICK_COUNT);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean(KEY_IS_PLAYING, this.isPlaying);
        pTag.putLong(KEY_RECORD_STARTED_TICK, this.recordStartedTick);
        pTag.putLong(KEY_TICK_COUNT, this.tickCount);
    }

    @Override
    protected String getItemNbtKey() {
        return "RecordItem";
    }
}
