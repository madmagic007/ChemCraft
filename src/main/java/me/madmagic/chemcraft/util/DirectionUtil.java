package me.madmagic.chemcraft.util;

import net.minecraft.core.Direction;

public class DirectionUtil {

    public static Direction facingToRelative(Direction facing, Direction absolute) {
        return Direction.from2DDataValue(
                (absolute.get2DDataValue() - facing.get2DDataValue() + 4) % 4).getOpposite();
    }
}
