package me.madmagic.chemcraft.instances.blocks.base;

import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyItemStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BaseBlock extends Block {

    public BaseBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.PASS;

        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (!(entity instanceof MenuProvider ent)) return InteractionResult.FAIL;

        NetworkHooks.openScreen((ServerPlayer) pPlayer, ent, pPos);

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            BlockEntity ent = pLevel.getBlockEntity(pPos);
            if (ent instanceof BaseEnergyItemStorageBlockEntity itemEnt) {
                itemEnt.dropContents();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}
