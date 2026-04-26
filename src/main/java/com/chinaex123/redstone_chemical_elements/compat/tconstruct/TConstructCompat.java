package com.chinaex123.redstone_chemical_elements.compat.tconstruct;

import com.chinaex123.redstone_chemical_elements.RedstonechanChemicalElements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;

public final class TConstructCompat {
    private static final String TCONSTRUCT_MODID = "tconstruct";
    private static final int VALUE_PLATE = Material.VALUE_Ingot;
    private static final int VALUE_ROD = 72;

    private static boolean preInitialized;
    private static boolean initialized;
    private static final Map<String, RceConfigurableTrait> TRAITS = new LinkedHashMap<>();
    private static final List<MaterialIntegration> INTEGRATIONS = new ArrayList<>();

    private TConstructCompat() {
    }

    public static boolean isAvailable() {
        return Loader.isModLoaded(TCONSTRUCT_MODID);
    }

    public static synchronized void preInit() {
        if (preInitialized || !isAvailable()) {
            return;
        }

        preInitialized = true;

        List<TConstructDataLoader.TraitDefinition> traitDefinitions = TConstructDataLoader.loadTraits();
        List<TConstructDataLoader.MaterialDefinition> materialDefinitions = TConstructDataLoader.loadMaterials();

        registerTraits(traitDefinitions);
        registerMaterials(materialDefinitions);

        for (MaterialIntegration integration : INTEGRATIONS) {
            integration.preInit();
        }

        RedstonechanChemicalElements.LOGGER.info(
            "Registered built-in optional TConstruct compat: {} traits, {} materials.",
            TRAITS.size(),
            INTEGRATIONS.size()
        );
    }

    public static synchronized void init() {
        if (initialized || !isAvailable()) {
            return;
        }

        initialized = true;
        int integratedCount = 0;
        for (MaterialIntegration integration : INTEGRATIONS) {
            integration.integrate();
            if (integration.isIntegrated()) {
                integratedCount++;
            }
        }

        RedstonechanChemicalElements.LOGGER.info(
            "Integrated {} / {} built-in TConstruct materials.",
            integratedCount,
            INTEGRATIONS.size()
        );
    }

    private static void registerTraits(List<TConstructDataLoader.TraitDefinition> definitions) {
        for (TConstructDataLoader.TraitDefinition definition : definitions) {
            RceConfigurableTrait trait = new RceConfigurableTrait(definition);
            TRAITS.put(definition.identifier, trait);
            TinkerRegistry.addTrait(trait);
        }
    }

    private static void registerMaterials(List<TConstructDataLoader.MaterialDefinition> definitions) {
        for (TConstructDataLoader.MaterialDefinition definition : definitions) {
            RceLocalizedMaterial material = new RceLocalizedMaterial(
                definition.identifier,
                definition.color,
                definition.localizedName
            );
            material.setRenderInfo(definition.color);
            material.setCraftable(true);
            material.setRepresentativeItem("ingot" + definition.oreBase);
            material.addStats(new HeadMaterialStats(
                definition.headDurability,
                definition.miningSpeed,
                definition.attack,
                definition.harvestLevel
            ));
            material.addStats(new HandleMaterialStats(definition.handleModifier, definition.handleDurability));
            material.addStats(new ExtraMaterialStats(definition.extraDurability));

            registerMaterialItems(material, definition.oreBase);
            applyTraits(material, definition.headTraits, MaterialTypes.HEAD);
            applyTraits(material, definition.handleTraits, MaterialTypes.HANDLE);
            applyTraits(material, definition.extraTraits, MaterialTypes.EXTRA);

            Fluid fluid = FluidRegistry.getFluid(definition.fluidName);
            if (fluid == null) {
                RedstonechanChemicalElements.LOGGER.warn(
                    "Missing fluid {} for TConstruct material {}, falling back to craftable-only integration.",
                    definition.fluidName,
                    definition.identifier
                );
            }

            MaterialIntegration integration = fluid == null
                ? TinkerRegistry.integrate(new MaterialIntegration(material).toolforge())
                : TinkerRegistry.integrate(new MaterialIntegration(material, fluid, definition.oreBase).toolforge());
            INTEGRATIONS.add(integration);
        }
    }

    private static void registerMaterialItems(Material material, String oreBase) {
        material.addItem("ingot" + oreBase, 1, Material.VALUE_Ingot);
        material.addItem("nugget" + oreBase, 1, Material.VALUE_Nugget);
        material.addItem("block" + oreBase, 1, Material.VALUE_Block);
        material.addItem("plate" + oreBase, 1, VALUE_PLATE);
        material.addItem("rod" + oreBase, 1, VALUE_ROD);
    }

    private static void applyTraits(Material material, List<String> traitIds, String statType) {
        for (String traitId : safeList(traitIds)) {
            RceConfigurableTrait trait = TRAITS.get(traitId);
            if (trait == null) {
                RedstonechanChemicalElements.LOGGER.warn(
                    "Unknown TConstruct trait {} referenced by material {}",
                    traitId,
                    material.getIdentifier()
                );
                continue;
            }
            material.addTrait(trait, statType);
        }
    }

    private static List<String> safeList(List<String> values) {
        return values == null ? Collections.emptyList() : values;
    }
}
