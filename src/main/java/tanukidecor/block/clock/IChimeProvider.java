/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.clock;

import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.Random;

public interface IChimeProvider {

    public static final long DAWN = 0L;
    public static final long NOON = 6000L;
    public static final long MIDNIGHT = 18000L;
    public static final long MIN_CHIME_INTERVAL = 40L;

    /** @return the chime sound, if any **/
    @Nullable default SoundEvent getChimeSound() {
        return null;
    }

    /** @return the tick sound, if any **/
    @Nullable default SoundEvent getTickSound() {
        return null;
    }

    /** @return the number of ticks to wait before playing the next tick sound **/
    default int getTickSoundInterval() {
        return 20;
    }

    /**
     * @param dayTime the day time
     * @return true to play a chime sound in this tick
     */
    default boolean isTimeToChime(final long dayTime) {
        return dayTime == NOON || dayTime == (NOON + MIN_CHIME_INTERVAL) || dayTime == MIDNIGHT;
    }

    /**
     * @param random the random instance
     * @param dayTime the day time from 0 to 24000
     * @return the volume of the chime sound
     */
    default float getChimeVolume(Random random, long dayTime) {
        return 1.0F;
    }

    /**
     * @param random the random instance
     * @param dayTime the day time from 0 to 24000
     * @return the pitch of the chime sound
     */
    default float getChimePitch(Random random, long dayTime) {
        return 1.0F;
    }

    /**
     * @param random the random instance
     * @param dayTime the day time from 0 to 24000
     * @return the volume of the tick sound
     */
    default float getTickVolume(Random random, long dayTime) {
        return 0.6F;
    }

    /**
     * @param random the random instance
     * @param dayTime the day time from 0 to 24000
     * @return the pitch of the tick sound
     */
    default float getTickPitch(Random random, long dayTime) {
        return 1.0F;
    }
}
