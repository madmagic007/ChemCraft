package me.madmagic.chemcraft.util.pipes;

import me.madmagic.chemcraft.ChemCraft;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.awt.event.MouseAdapter;

public class PipeWrenchHandler {

    private static final double thresholdABS = 0.75;
    public static Direction getClickedDirection(BlockHitResult hitResult) {
        Vec3 vec = hitResult.getLocation();

        double decX = fixCoord(vec.x % 1);
        double decY = fixCoord(vec.y % 1);
        double decZ = fixCoord(vec.z % 1);

        Direction affectedDir = hitResult.getDirection();

        if (decX > thresholdABS) affectedDir = Direction.EAST;
        else if (decX < 1 - thresholdABS) affectedDir = Direction.WEST;

        else if (decY > thresholdABS) affectedDir = Direction.UP;
        else if (decY < 1 - thresholdABS) affectedDir = Direction.DOWN;

        else if (decZ > thresholdABS) affectedDir = Direction.SOUTH;
        else if (decZ < 1 - thresholdABS) affectedDir = Direction.NORTH;

        return affectedDir;
    }
    
    private static double fixCoord(double original) {
        if (original < 0) return 1 - Math.abs(original);
        return original;
    }
}
