package me.madmagic.chemcraft.util.multiblock.instances;

import me.madmagic.chemcraft.instances.blockentities.DistilleryBlockEntity;
import me.madmagic.chemcraft.instances.blocks.DistilleryBlock;
import me.madmagic.chemcraft.instances.blocks.InsulatedBlock;
import me.madmagic.chemcraft.instances.blocks.SievePlateBlock;
import me.madmagic.chemcraft.util.GeneralUtil;
import me.madmagic.chemcraft.util.multiblock.MultiBlockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.phys.AABB;

public class DistilleryMultiBlock extends MultiBlockStructure {


    public DistilleryMultiBlock(BlockPos masterPos, Level level) {
        super(masterPos, level);
        addAllConnectedOfType(InsulatedBlock.class, SievePlateBlock.class);
    }

    @Override
    public void created(boolean isExisting) {
        super.created(isExisting);
        DistilleryBlockEntity ent = (DistilleryBlockEntity) level.getBlockEntity(masterPos);
        if (ent == null) return; //just incase...

        int xSize = (int) Math.sqrt(blocks.firstEntry().getValue().size());
        ent.multiBlockCreated(blocks.firstKey(), blocks.size(), (int) Math.pow(xSize, 2));
        ent.setChanged();
    }

    @Override
    public boolean validate() {
        int bottomLevel = blocks.firstKey();
        AABB bottomDim = dimAtLevel(bottomLevel);

        int topLevel = blocks.lastKey();
        AABB topDim = dimAtLevel(topLevel);

        int lastToTopLevel = topLevel - 1;
        AABB lastToTopDim = dimAtLevel(lastToTopLevel);

        if (topLevel - bottomLevel < 3 ||
                !isFilled(bottomDim, InsulatedBlock.class, DistilleryBlock.class) ||
                !isFilled(topDim, InsulatedBlock.class) ||
                !isCenterFilled(lastToTopDim, AirBlock.class) ||
                bottomDim.getXsize() < 2) return false;

        for (int level : blocks.keySet()) {
            AABB dim = dimAtLevel(level);
            if (!isSame(bottomDim, dim)) return false;

            if (GeneralUtil.isAny(level, bottomLevel, lastToTopLevel, topLevel)) continue;
            if (!isCenterFilled(dim, SievePlateBlock.class)) return false;
        }

        AABB combined = new AABB(bottomDim.minX, bottomDim.minY, bottomDim.minZ, topDim.maxX, topDim.maxY, topDim.maxZ);
        return isSquare(combined, false) && isFilled(combined, InsulatedBlock.class, DistilleryBlock.class, SievePlateBlock.class, AirBlock.class);
    }
}
