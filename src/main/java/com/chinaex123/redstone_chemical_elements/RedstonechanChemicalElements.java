package com.chinaex123.redstone_chemical_elements;

import com.chinaex123.redstone_chemical_elements.register.ElementCatalog;
import com.chinaex123.redstone_chemical_elements.register.DerivedFluidRegistry;
import com.chinaex123.redstone_chemical_elements.register.ElementFluidRegistry;
import com.chinaex123.redstone_chemical_elements.register.ModRecipes;
import java.io.File;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = RedstonechanChemicalElements.MODID,
        name = RedstonechanChemicalElements.NAME,
        version = RedstonechanChemicalElements.VERSION,
        dependencies = "after:mantle;after:tconstruct"
)
public class RedstonechanChemicalElements {
    public static final String MODID = "redstone_chemical_elements";
    public static final String NAME = "Redstone-chan Chemical Elements";
    public static final String VERSION = "1.1.0";
    private static final String[] LEGACY_TCONSTRUCT_SCRIPT_FILES = {
        "00_rce_tconstruct_traits.zs",
        "rce_tconstruct_base_materials.zs",
        "rce_tconstruct_materials.zs"
    };

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    private static boolean legacyTConstructScriptsDetected;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ElementFluidRegistry.bootstrap();
        DerivedFluidRegistry.bootstrap();
        legacyTConstructScriptsDetected = hasLegacyTConstructScripts(event.getModConfigurationDirectory());
        invokeOptionalTConstructCompat("preInit");
        LOGGER.info("{} preInit, registered {} elemental fluids.", NAME, ElementCatalog.ELEMENTS.length);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModRecipes.registerSmelting();
        invokeOptionalTConstructCompat("init");
        LOGGER.info("{} init", NAME);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("{} postInit", NAME);
    }

    private static void invokeOptionalTConstructCompat(String methodName) {
        if (!Loader.isModLoaded("tconstruct") || legacyTConstructScriptsDetected) {
            return;
        }

        try {
            Class<?> compatClass = Class.forName(
                "com.chinaex123.redstone_chemical_elements.compat.tconstruct.TConstructCompat"
            );
            compatClass.getMethod(methodName).invoke(null);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to invoke optional TConstruct compat: " + methodName, exception);
        }
    }

    private static boolean hasLegacyTConstructScripts(File configDirectory) {
        File minecraftDirectory = configDirectory == null ? null : configDirectory.getParentFile();
        File scriptsDirectory = minecraftDirectory == null ? null : new File(minecraftDirectory, "scripts");
        if (scriptsDirectory == null || !scriptsDirectory.isDirectory()) {
            return false;
        }

        for (String scriptName : LEGACY_TCONSTRUCT_SCRIPT_FILES) {
            if (new File(scriptsDirectory, scriptName).isFile()) {
                LOGGER.warn(
                    "Detected legacy CraftTweaker TConstruct script [{}]. Built-in TConstruct compat is disabled to avoid duplicate registration.",
                    scriptName
                );
                return true;
            }
        }
        return false;
    }
}
