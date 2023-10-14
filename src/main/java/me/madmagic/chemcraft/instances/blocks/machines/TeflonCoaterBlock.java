package me.madmagic.chemcraft.instances.blocks.machines;

import me.madmagic.chemcraft.instances.blockentities.machines.TeflonCoaterBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.BaseBlock;
import me.madmagic.chemcraft.instances.blocks.base.blocktypes.IRotateAble;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TeflonCoaterBlock extends BaseBlock implements AutoEntityTickerBlock, IRotateAble {

    public TeflonCoaterBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TeflonCoaterBlockEntity(pPos, pState);
    }
}
