package me.madmagic.chemcraft.instances.items;

import me.madmagic.chemcraft.instances.items.base.BaseItem;
import me.madmagic.chemcraft.instances.menus.ChemistsManualScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChemistsManual extends BaseItem {

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            Minecraft.getInstance().setScreen(new ChemistsManualScreen());

            ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
