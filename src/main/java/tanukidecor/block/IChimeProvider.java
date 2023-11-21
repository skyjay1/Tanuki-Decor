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

    @Nullable SoundEvent getChimeSound();

    @Nullable SoundEvent getTickSound();

    default float getChimeVolume(Random random, long dayTime) {
        return 1.0F;
    }

    default float getChimePitch(Random random, long dayTime) {
        return 1.0F;
    }

    default float getTickVolume(Random random, long dayTime) {
        return 0.6F;
    }

    default float getTickPitch(Random random, long dayTime) {
        return 0.9F + ((dayTime / 20) % 2) * 0.1F; // TODO fix math so every other tick is different pitch
    }
}
