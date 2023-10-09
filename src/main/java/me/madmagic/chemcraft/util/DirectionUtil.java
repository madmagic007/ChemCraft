package me.madmagic.chemcraft.util;

import net.minecraft.core.Direction;

import java.util.function.Consumer;

public class DirectionUtil {

    public static final Direction[] directions = Direction.values();

    public static void forEach(Consumer<Direction> directionConsumer) {
        for (Direction direction : directions) {
            directionConsumer.accept(direction);
        }
    }

    public static void forEachHorizontal(Consumer<Direction> directionConsumer) {
        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                directionConsumer.accept(direction);
            }
        }
    }
}
