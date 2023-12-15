/*
 * Copyright (c) 2023 Skyler James
 * Permission is granted to use, modify, and redistribute this software, in parts or in whole,
 * under the GNU LGPLv3 license (https://www.gnu.org/licenses/lgpl-3.0.en.html)
 */

package tanukidecor.block.storage;

import tanukidecor.TDRegistry;

public class BlueBookshelfBlock extends WideStorageBlock {

    public BlueBookshelfBlock(Properties pProperties) {
        super(BlueBureauBlock.SHAPE_EAST, BlueBureauBlock.SHAPE_WEST, TDRegistry.BlockEntityReg.BLUE_BOOKSHELF, pProperties);
    }
}
