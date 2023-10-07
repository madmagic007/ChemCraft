package me.madmagic.chemcraft.instances.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.Arrays;
import java.util.List;

public abstract class BaseCommand {

    protected abstract ArgumentBuilder<CommandSourceStack, ?> register();

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("chemcraft")
                .requires(p -> p.hasPermission(2));

        commands.forEach(command -> builder.then(command.register()));

        pDispatcher.register(builder);

    }

    private static final List<BaseCommand> commands = Arrays.asList(
            new NoEnergyUsageCommand(), new NoFuelUsageCommand()
    );
}
