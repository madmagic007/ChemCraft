package me.madmagic.chemcraft;

import com.mojang.logging.LogUtils;
import me.madmagic.chemcraft.instances.CustomBlockEntities;
import me.madmagic.chemcraft.instances.CustomBlocks;
import me.madmagic.chemcraft.instances.CustomItems;
import me.madmagic.chemcraft.instances.CustomMenus;
import me.madmagic.chemcraft.instances.commands.BaseCommand;
import me.madmagic.chemcraft.util.DynAssetGenTest;
import me.madmagic.chemcraft.util.networking.NetworkSender;
import me.madmagic.chemcraft.util.reloaders.ChemicalReactionRegisterer;
import me.madmagic.chemcraft.util.reloaders.FluidRegisterer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
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

        if (FMLEnvironment.dist.isClient())
            DynAssetGenTest.test();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetworkSender.defineMessages();
    }

    @Mod.EventBusSubscriber(modid = ChemCraft.modId, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void addReloadListener(AddReloadListenerEvent event) {
            event.addListener(new FluidRegisterer());
            event.addListener(new ChemicalReactionRegisterer());
        }

        @SubscribeEvent
        public static void onClientSetup(RegisterCommandsEvent event) {
            BaseCommand.register(event.getDispatcher());
        }
    }


    @Mod.EventBusSubscriber(modid = ChemCraft.modId, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CustomMenus.setupScreens();
        }
    }
}