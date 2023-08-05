package me.madmagic.chemcraft.util.multiblock.instances;

import me.madmagic.chemcraft.instances.blocks.InsulatedBlock;
import me.madmagic.chemcraft.instances.blocks.TankControllerBlock;
import me.madmagic.chemcraft.instances.blockentities.TankControllerBlockEntity;
import me.madmagic.chemcraft.util.fluids.IFluidContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class TankMultiBlock extends MultiBlockStructure {

    public TankMultiBlock(BlockPos masterPos, Level level) {
        super(masterPos, level);
        addAllConnectedOfType(InsulatedBlock.class);
    }

    @Override
    public void created(boolean isExisting) {
        super.created(isExisting);

        BlockEntity entity = level.getBlockEntity(masterPos);
        if (!(entity instanceof IFluidContainer container)) return;
        container.getFluidStorage().capacity = allBlocks.size() * TankControllerBlockEntity.capacityPerTank;
    }

    @Override
    protected boolean validate() {
        AABB baseDim = dimAtLevel(blocks.firstKey());
        AABB topDim = dimAtLevel(blocks.lastKey());

        if (!isSquare(baseDim, false) || !isFilled(baseDim)) return false;

        for (int key : blocks.keySet()) {
            AABB dim = dimAtLevel(key);
            if (!isSame(baseDim, dim)) return false;
            topDim = dim;
        }

        AABB combined = new AABB(baseDim.minX, baseDim.minY, baseDim.minZ, topDim.maxX, topDim.maxY, topDim.maxZ);

        return isSquare(combined, false) && isFilled(combined, TankControllerBlock.class, InsulatedBlock.class);
    }
}
