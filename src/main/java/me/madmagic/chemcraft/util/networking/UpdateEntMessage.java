package me.madmagic.chemcraft.util.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class UpdateEntMessage {

    private final BlockPos pos;
    private final int[] values;

    public UpdateEntMessage(BlockPos pos, int... values) {
        this.pos = pos;
        this.values = values;
    }

    public UpdateEntMessage(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        values = buf.readVarIntArray();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeVarIntArray(values);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BlockEntity entity = context.getSender().level().getBlockEntity(pos);
            if (!(entity instanceof INetworkUpdateAble ent)) return;

            ent.updateFromNetworking(values);
            entity.setChanged();
        });
        return true;
    }

    public static void define(SimpleChannel channel, int id) {
        channel.messageBuilder(UpdateEntMessage.class, id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateEntMessage::new)
                .encoder(UpdateEntMessage::encode)
                .consumerMainThread(UpdateEntMessage::handle)
                .add();
    }
}
