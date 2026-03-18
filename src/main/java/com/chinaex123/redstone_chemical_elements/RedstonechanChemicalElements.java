package com.chinaex123.redstone_chemical_elements;

import com.chinaex123.redstone_chemical_elements.register.ModRecipes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = RedstonechanChemicalElements.MODID,
        name = RedstonechanChemicalElements.NAME,
        version = RedstonechanChemicalElements.VERSION
)
public class RedstonechanChemicalElements {
    public static final String MODID = "redstone_chemical_elements";
    public static final String NAME = "Redstone-chan Chemical Elements";
    public static final String VERSION = "1.1.1-1.12.2";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("{} preInit", NAME);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModRecipes.registerSmelting();
        LOGGER.info("{} init", NAME);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("{} postInit", NAME);
    }
}
