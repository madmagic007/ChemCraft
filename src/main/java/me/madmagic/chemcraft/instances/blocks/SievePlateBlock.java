package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SievePlateBlock extends BaseBlock {

    private static final VoxelShape shape = Block.box(0, 14, 0, 16, 16, 16);

    public SievePlateBlock() {
        super(
                BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                .noOcclusion()
                .forceSolidOn()
                .dynamicShape(),
                shape);
    }
}
