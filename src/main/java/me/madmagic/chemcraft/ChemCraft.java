package me.madmagic.chemcraft;

import com.mojang.logging.LogUtils;
import me.madmagic.chemcraft.instances.CustomBlocks;
import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ChemCraft.modId)
public class ChemCraft {
    public static final String modId = "chemcraft";
    public static final String modName = "ChemCraft";
    private static final Logger logger = LogUtils.getLogger();

    public static void info(Object info) {
        if (info == null) return;
        logger.info("ChemCraft: " + info);
    }

    public static void info(Object... info) {
        if (info == null) return;

        String s = "";
        for (Object o : info) {
            s += (o + ", ");
        }
        info(s);
    }

    public ChemCraft() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CustomItems.register(eventBus);
        CustomBlocks.register(eventBus);
        CustomBlockEntities.register(eventBus);
        CustomMenus.register(eventBus);

        eventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetworkSender.defineMessages();
    }

    @Mod.EventBusSubscriber(modid = ChemCraft.modId, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CustomMenus.setupScreens();
        }
    }
}