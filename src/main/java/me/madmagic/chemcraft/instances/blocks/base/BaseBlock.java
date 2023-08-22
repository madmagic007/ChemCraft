package me.madmagic.chemcraft.instances.blocks.base;

import me.madmagic.chemcraft.instances.blockentities.base.BaseBlockEntity;
import me.madmagic.chemcraft.instances.blockentities.base.BaseEnergyItemStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

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
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        try { //first forced upon, useless try catch I wrote, achievement unlocked
            BlockEntity ent = pParams.getParameter(LootContextParams.BLOCK_ENTITY);
            if (!(ent instanceof BaseBlockEntity blockEnt)) return super.getDrops(pState, pParams);

            ItemStack stack = new ItemStack(this);

            CompoundTag nbt = new CompoundTag();
            blockEnt.saveToNBT(nbt);
            nbt.remove("chemcraft.inventory");
            stack.addTagElement("BlockEntityTag", nbt);

            if (blockEnt instanceof BaseEnergyItemStorageBlockEntity itemEnt) itemEnt.dropContents();

            return List.of(stack);
        } catch (Exception ignored) {
            return super.getDrops(pState, pParams);
        }
    }
}
