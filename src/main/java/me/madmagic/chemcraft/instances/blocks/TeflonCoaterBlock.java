package me.madmagic.chemcraft.instances.blocks;

import me.madmagic.chemcraft.instances.blockentities.TeflonCoaterBlockEntity;
import me.madmagic.chemcraft.instances.blocks.base.AutoEntityTickerBlock;
import me.madmagic.chemcraft.instances.blocks.base.RotatableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TeflonCoaterBlock extends RotatableBlock implements AutoEntityTickerBlock {

    public TeflonCoaterBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TeflonCoaterBlockEntity(pPos, pState);
    }
}
