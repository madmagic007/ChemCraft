package me.madmagic.chemcraft.util.networking;

import me.madmagic.chemcraft.ChemCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkSender {

    private static int id = 0;
    private static int getId() {
        return id++;
    }

    private static final String protocolVersion = "1";
    private static final SimpleChannel channel = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ChemCraft.modId, "messages"))
            .networkProtocolVersion(() -> protocolVersion)
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

    public static <MSG> void sendToServer(MSG msg) {
        channel.sendToServer(msg);
    }

    public static <MSG> void sendToClient(ServerPlayer player, MSG msg) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void defineMessages() {
        UpdateEntMessage.define(channel, getId());
        SetRedstoneModeMessage.define(channel, getId());
    }
}
