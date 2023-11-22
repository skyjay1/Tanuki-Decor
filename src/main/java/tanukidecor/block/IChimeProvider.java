/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block;

import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.Random;

public interface IChimeProvider {

    /** @return the chime sound, if any **/
    @Nullable SoundEvent getChimeSound();

    /** @return the tick sound, if any **/
    @Nullable SoundEvent getTickSound();

    /** @return the number of ticks to wait before playing the next tick sound **/
    default int getTickSoundInterval() {
        return 20;
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
