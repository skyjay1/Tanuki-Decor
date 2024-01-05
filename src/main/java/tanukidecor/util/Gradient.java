/*
 * Copyright (c) 2024 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.util;

import com.mojang.math.Vector4f;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Gradient {

    private final List<Entry> colors;

    private Gradient(List<Entry> colors) {
        if(colors.isEmpty()) {
            throw new IllegalArgumentException("Failed to build Gradient with empty list");
        }
        this.colors = colors;
    }

    /**
     * @param percent a number from 0.0 to 1.0
     * @return the color for the given threshold
     */
    public Vector4f getColor(final float percent) {
        for(Entry entry : colors) {
            if(percent > entry.threshold) {
                return entry.color();
            }
        }
        return colors.get(colors.size() - 1).color();
    }

    //// HELPER METHODS ////

    public static Gradient.Builder builder(final int color) {
        return new Gradient.Builder(color);
    }

    /**
     * Separates a hex color into RGBA components.
     *
     * @param color a packed int RGBA color
     * @return the red, green, blue, and alpha components as a Vector4f
     **/
    public static Vector4f unpackColor(final int color) {
        long tmpColor = color;
        if ((tmpColor & -16777216) == 0) {
            tmpColor |= 0xFF000000; // Set alpha to 255 if it's not present
        }

        float colorRed = (float) ((tmpColor >> 16) & 255) / 255.0F;
        float colorGreen = (float) ((tmpColor >> 8) & 255) / 255.0F;
        float colorBlue = (float) (tmpColor & 255) / 255.0F;
        float colorAlpha = (float) ((tmpColor >> 24) & 255) / 255.0F;

        return new Vector4f(colorRed, colorGreen, colorBlue, colorAlpha);
    }

    public static class Builder {

        private final List<Entry> list;

        public Builder(int color) {
            this.list = new ArrayList<>();
            this.list.add(new Entry(0.0F, unpackColor(color)));
        }

        public Builder with(final float threshold, final int color) {
            this.list.add(new Entry(threshold, unpackColor(color)));
            return this;
        }

        public Gradient build() {
            // sort list
            this.list.sort(Comparator.reverseOrder());
            // create gradient
            return new Gradient(this.list);
        }
    }

    private static record Entry(float threshold, Vector4f color) implements Comparable<Entry> {
        @Override
        public int compareTo(@NotNull Gradient.Entry o) {
            return Float.compare(this.threshold, o.threshold);
        }
    }
}
