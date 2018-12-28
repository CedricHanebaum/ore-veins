/*
 *  Part of the Ore Veins Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package oreveins;


import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import oreveins.cmd.CommandClearWorld;
import oreveins.cmd.CommandFindVeins;
import oreveins.cmd.CommandVeinInfo;
import oreveins.world.WorldGenReplacer;
import oreveins.world.WorldGenVeins;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(modid = OreVeins.MOD_ID, version = "GRADLE:VERSION", dependencies = "required-after:forge@[GRADLE:FORGE_VERSION,15.0.0.0);", acceptableRemoteVersions = "*")
public class OreVeins
{
    public static final String MOD_ID = "oreveins";

    private static Logger log;

    public static Logger getLog()
    {
        return log;
    }

    // This is necessary in order to catch the NewRegistry Event
    public OreVeins()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MOD_ID))
        {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
            WorldGenVeins.resetSearchRadius();
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        log = event.getModLog();

        RegistryManager.preInit(event.getModConfigurationDirectory());

        GameRegistry.registerWorldGenerator(new WorldGenVeins(), 1);
        MinecraftForge.ORE_GEN_BUS.register(new WorldGenReplacer());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        RegistryManager.registerAllVeins();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        if (OreVeinsConfig.DEBUG_COMMANDS)
        {
            event.registerServerCommand(new CommandClearWorld());
            event.registerServerCommand(new CommandVeinInfo());
            event.registerServerCommand(new CommandFindVeins());
        }
    }
}
