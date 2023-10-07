package me.madmagic.chemcraft.instances.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import me.madmagic.chemcraft.util.ChemCraftSaveData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class NoFuelUsageCommand extends BaseCommand {

    @Override
    protected ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("disableFuelUsage")
                .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(c -> {
                            boolean value = BoolArgumentType.getBool(c, "value");
                            ChemCraftSaveData saveData = ChemCraftSaveData.getOrCreate(c.getSource().getLevel());
                            saveData.isFuelUsageDisabled = value;
                            saveData.setDirty();

                            if (value) c.getSource().sendSuccess(() -> Component.literal("Disabled fuel usage"), false);
                            else c.getSource().sendSuccess(() -> Component.literal("Enabled fuel usage"), false);
                            return 1;
                        })
                );
    }
}
