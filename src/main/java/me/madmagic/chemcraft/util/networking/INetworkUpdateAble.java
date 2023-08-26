package me.madmagic.chemcraft.util.networking;

public interface INetworkUpdateAble {

    default void updateFromNetworking(int... values) {}
}
