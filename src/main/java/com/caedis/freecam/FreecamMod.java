package com.caedis.freecam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caedis.freecam.config.GeneralConfig;
import com.caedis.freecam.config.MiscConfig;
import com.caedis.freecam.config.MovementConfig;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(
    modid = FreecamMod.MODID,
    version = Tags.VERSION,
    name = FreecamMod.MODNAME,
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*")
public class FreecamMod {

    public static final String MODID = "freecam";
    public static final String MODNAME = "Freecam";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            ConfigurationManager.registerConfig(GeneralConfig.class);
            ConfigurationManager.registerConfig(MovementConfig.class);
            ConfigurationManager.registerConfig(MiscConfig.class);
        } catch (ConfigException e) {
            LOG.error("Failed to register config", e);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLCommonHandler.instance()
            .getSide() == Side.CLIENT) {
            ClientEventHandler clientHandler = new ClientEventHandler();
            clientHandler.init();
            FMLCommonHandler.instance()
                .bus()
                .register(clientHandler);
        }
    }
}
