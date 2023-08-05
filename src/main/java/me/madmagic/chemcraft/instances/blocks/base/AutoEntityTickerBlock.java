package me.madmagic.chemcraft.instances.blocks.base;

import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface AutoEntityTickerBlock extends EntityBlock {

    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : (l, p, s, ent) -> ((BaseBlockEntity) ent).tick();
    }
}
