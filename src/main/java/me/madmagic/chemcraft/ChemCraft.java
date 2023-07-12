package me.madmagic.chemcraft;

import com.mojang.logging.LogUtils;
import me.madmagic.chemcraft.instances.blocks.base.BlockHandler;
import me.madmagic.chemcraft.instances.blocks.entity.base.BlockEntityHandler;
import me.madmagic.chemcraft.instances.items.base.ItemHandler;
import me.madmagic.chemcraft.instances.menus.base.MenuHandler;
import me.madmagic.chemcraft.instances.menus.base.ScreenHandler;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkInstance;
import net.minecraftforge.network.NetworkRegistry;
import org.slf4j.Logger;

@Mod(ChemCraft.modId)
public class ChemCraft {
    public static final String modId = "chemcraft";
    public static final String modName = "ChemCraft";
    private static final Logger logger = LogUtils.getLogger();

    public static void info(Object info) {
        if (info == null) return;
        logger.info("MadCraft: " + info);
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

        ItemHandler.register(eventBus);
        BlockHandler.register(eventBus);
        BlockEntityHandler.register(eventBus);
        MenuHandler.register(eventBus);

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
            ScreenHandler.setup();
        }
    }
}