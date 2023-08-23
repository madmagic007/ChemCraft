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
    private final int value;

    public UpdateEntMessage(BlockPos pos, int value) {
        this.pos = pos;
        this.value = value;
    }

    public UpdateEntMessage(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        value = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(value);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            BlockEntity entity = context.getSender().level().getBlockEntity(pos);
            if (!(entity instanceof INetworkUpdateAble ent)) return;

            ent.updateFromNetworking(value);
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
